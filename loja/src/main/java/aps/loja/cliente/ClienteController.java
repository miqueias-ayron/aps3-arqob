package aps.loja.cliente;

import aps.loja.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable int id) {
        return clienteService.findById(id);
    }

    @GetMapping("/{id}/pedidos")
    public List<Pedido> listarPedidos(@PathVariable int id) {
        return clienteService.listarPedidos(id);
    }

    @GetMapping("/{id}/total-gasto")
    public BigDecimal calcularTotalGasto(@PathVariable int id) {
        return clienteService.calcularTotalGasto(id);
    }

    @GetMapping("/{id}/pedido-mais-caro")
    public ResponseEntity<Pedido> buscarPedidoMaisCaro(@PathVariable int id) {
        Pedido pedido = clienteService.buscarPedidoMaisCaro(id);
        return pedido == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedido);
    }
}
