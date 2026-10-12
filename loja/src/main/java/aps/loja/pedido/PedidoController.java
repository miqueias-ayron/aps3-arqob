package aps.loja.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public Pedido create(@RequestParam int clienteId) {
        return pedidoService.create(clienteId);
    }

    @GetMapping("/{numero}")
    public Pedido findById(@PathVariable int numero) {
        return pedidoService.findById(numero);
    }

    @PostMapping("/{numero}/itens")
    public Pedido adicionarProduto(@PathVariable int numero,
                                   @RequestParam int produtoCodigo,
                                   @RequestParam int quantidade) {
        return pedidoService.adicionarProduto(numero, produtoCodigo, quantidade);
    }

    @DeleteMapping("/{numero}/itens/{produtoCodigo}")
    public Pedido removerProduto(@PathVariable int numero, @PathVariable int produtoCodigo) {
        return pedidoService.removerProduto(numero, produtoCodigo);
    }

    @GetMapping("/{numero}/total")
    public BigDecimal calcularTotal(@PathVariable int numero) {
        return pedidoService.calcularTotal(numero);
    }

    @GetMapping("/{numero}/quantidade-itens")
    public int calcularQuantidadeItens(@PathVariable int numero) {
        return pedidoService.calcularQuantidadeItens(numero);
    }

    @GetMapping("/{numero}/item-mais-caro")
    public ResponseEntity<ItemPedido> buscarItemMaisCaro(@PathVariable int numero) {
        ItemPedido item = pedidoService.buscarItemMaisCaro(numero);
        return item == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(item);
    }

    @PutMapping("/{numero}/status")
    public Pedido alterarStatus(@PathVariable int numero, @RequestParam String status) {
        return pedidoService.alterarStatus(numero, status);
    }
}
