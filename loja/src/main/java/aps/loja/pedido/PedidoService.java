package aps.loja.pedido;

import aps.loja.cliente.ClienteService;
import aps.loja.produto.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public Pedido create(int clienteId) {
        Pedido pedido = new Pedido();
        clienteService.findById(clienteId).adicionarPedido(pedido);
        return pedidoRepository.save(pedido);
    }

    public Pedido findById(int numero) {
        return pedidoRepository.findById(numero)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    @Transactional
    public Pedido adicionarProduto(int numero, int produtoCodigo, int quantidade) {
        Pedido pedido = findById(numero);
        pedido.adicionarProduto(produtoService.findById(produtoCodigo), quantidade);
        // flush para os itens novos receberem id antes da serialização
        return pedidoRepository.saveAndFlush(pedido);
    }

    @Transactional
    public Pedido removerProduto(int numero, int produtoCodigo) {
        Pedido pedido = findById(numero);
        pedido.removerProduto(produtoService.findById(produtoCodigo));
        return pedido;
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(int numero) {
        return findById(numero).calcularTotal();
    }

    @Transactional(readOnly = true)
    public int calcularQuantidadeItens(int numero) {
        return findById(numero).calcularQuantidadeItens();
    }

    @Transactional(readOnly = true)
    public ItemPedido buscarItemMaisCaro(int numero) {
        return findById(numero).buscarItemMaisCaro();
    }

    @Transactional
    public Pedido alterarStatus(int numero, String status) {
        Pedido pedido = findById(numero);
        pedido.alterarStatus(status);
        return pedido;
    }
}
