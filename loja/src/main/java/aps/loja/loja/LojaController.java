package aps.loja.loja;

import aps.loja.cliente.Cliente;
import aps.loja.produto.Produto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    @Autowired
    private LojaService lojaService;

    @PostMapping
    public Loja create(@Valid @RequestBody Loja loja) {
        return lojaService.create(loja);
    }

    @GetMapping
    public List<Loja> findAll() {
        return lojaService.findAll();
    }

    @GetMapping("/{id}")
    public Loja findById(@PathVariable int id) {
        return lojaService.findById(id);
    }

    @PostMapping("/{id}/produtos")
    public Produto adicionarProduto(@PathVariable int id,
                                    @RequestParam int categoriaId,
                                    @Valid @RequestBody Produto produto) {
        return lojaService.adicionarProduto(id, categoriaId, produto);
    }

    @GetMapping("/{id}/produtos")
    public List<Produto> listarProdutos(@PathVariable int id) {
        return lojaService.listarProdutos(id);
    }

    @GetMapping("/{id}/produtos/{codigo}")
    public Produto buscarProduto(@PathVariable int id, @PathVariable int codigo) {
        return lojaService.buscarProduto(id, codigo);
    }

    @GetMapping("/{id}/produtos/categoria/{categoriaId}")
    public List<Produto> listarProdutosPorCategoria(@PathVariable int id, @PathVariable int categoriaId) {
        return lojaService.listarProdutosPorCategoria(id, categoriaId);
    }

    @PostMapping("/{id}/clientes")
    public Cliente adicionarCliente(@PathVariable int id, @Valid @RequestBody Cliente cliente) {
        return lojaService.adicionarCliente(id, cliente);
    }

    @GetMapping("/{id}/clientes/busca")
    public Cliente buscarCliente(@PathVariable int id, @RequestParam String cpf) {
        return lojaService.buscarCliente(id, cpf);
    }
}
