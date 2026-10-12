package aps.loja.cliente;

import aps.loja.loja.Loja;
import aps.loja.pedido.Pedido;
import aps.loja.pedido.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_loja")
    private Loja loja;

    public void adicionarPedido(Pedido pedido) {
        pedido.setCliente(this);
        pedidos.add(pedido);
    }

    public List<Pedido> listarPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    // Pedidos cancelados não contam como gasto
    public BigDecimal calcularTotalGasto() {
        return pedidos.stream()
                .filter(pedido -> pedido.getStatus() != StatusPedido.CANCELADO)
                .map(Pedido::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retorna o pedido de maior valor total, ou null se o cliente não tiver pedidos.
     */
    public Pedido buscarPedidoMaisCaro() {
        return pedidos.stream()
                .max(Comparator.comparing(Pedido::calcularTotal))
                .orElse(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }
}
