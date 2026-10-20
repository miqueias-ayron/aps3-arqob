package aps.loja.categoria;

import aps.loja.produto.Produto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public Categoria create(@Valid @RequestBody Categoria categoria) {
        return categoriaService.create(categoria);
    }

    @GetMapping
    public List<Categoria> findAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{codigo}")
    public Categoria findById(@PathVariable int codigo) {
        return categoriaService.findById(codigo);
    }

    @PostMapping("/{codigo}/produtos/{produtoCodigo}")
    public List<Produto> adicionarProduto(@PathVariable int codigo, @PathVariable int produtoCodigo) {
        return categoriaService.adicionarProduto(codigo, produtoCodigo);
    }

    @GetMapping("/{codigo}/produtos")
    public List<Produto> listarProdutos(@PathVariable int codigo) {
        return categoriaService.listarProdutos(codigo);
    }

    @GetMapping("/{codigo}/preco-medio")
    public BigDecimal calcularPrecoMedio(@PathVariable int codigo) {
        return categoriaService.calcularPrecoMedio(codigo);
    }

    @GetMapping("/{codigo}/produto-mais-caro")
    public ResponseEntity<Produto> buscarProdutoMaisCaro(@PathVariable int codigo) {
        Produto produto = categoriaService.buscarProdutoMaisCaro(codigo);
        return produto == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(produto);
    }
}
