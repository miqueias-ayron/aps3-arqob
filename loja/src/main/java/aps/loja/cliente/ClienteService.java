package aps.loja.cliente;

import aps.loja.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findById(int id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos(int id) {
        return List.copyOf(findById(id).listarPedidos());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalGasto(int id) {
        return findById(id).calcularTotalGasto();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPedidoMaisCaro(int id) {
        return findById(id).buscarPedidoMaisCaro();
    }
}
