package aps.loja.pedido;

import aps.loja.cliente.Cliente;
import aps.loja.produto.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numero;

    @NotNull
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.ABERTO;

    public void adicionarProduto(Produto produto, int quantidade) {
        validarAberto();
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (!cliente.getLoja().equals(produto.getLoja())) {
            throw new IllegalArgumentException("Produto não pertence à loja do cliente");
        }
        // Produto repetido soma na quantidade e mantém o preço registrado na primeira inclusão
        buscarItem(produto).ifPresentOrElse(
                item -> item.adicionarQuantidade(quantidade),
                () -> itens.add(new ItemPedido(this, produto, quantidade)));
    }

    public void removerProduto(Produto produto) {
        validarAberto();
        ItemPedido item = buscarItem(produto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não está no pedido"));
        itens.remove(item);
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int calcularQuantidadeItens() {
        return itens.stream()
                .mapToInt(ItemPedido::getQuantidade)
                .sum();
    }

    /**
     * Retorna o item de maior subtotal, ou null se o pedido estiver vazio.
     */
    public ItemPedido buscarItemMaisCaro() {
        return itens.stream()
                .max(Comparator.comparing(ItemPedido::calcularSubtotal))
                .orElse(null);
    }

    public void alterarStatus(String status) {
        StatusPedido novoStatus;
        try {
            novoStatus = StatusPedido.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        if (this.status == StatusPedido.ENTREGUE || this.status == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("Pedido " + this.status + " não pode mudar de status");
        }
        if (novoStatus != StatusPedido.CANCELADO && itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido sem itens só pode ser cancelado");
        }
        this.status = novoStatus;
    }

    private Optional<ItemPedido> buscarItem(Produto produto) {
        return itens.stream()
                .filter(item -> item.getProduto().equals(produto))
                .findFirst();
    }

    private void validarAberto() {
        if (status != StatusPedido.ABERTO) {
            throw new IllegalArgumentException("Itens só podem ser alterados com o pedido ABERTO");
        }
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public StatusPedido getStatus() {
        return status;
    }
}
