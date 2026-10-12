package aps.loja.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> findAll() {
        return produtoService.findAll();
    }

    @GetMapping("/{codigo}")
    public Produto findById(@PathVariable int codigo) {
        return produtoService.findById(codigo);
    }

    @PutMapping("/{codigo}/desconto")
    public Produto aplicarDesconto(@PathVariable int codigo, @RequestParam double percentual) {
        return produtoService.aplicarDesconto(codigo, percentual);
    }

    @PutMapping("/{codigo}/aumento")
    public Produto aumentarPreco(@PathVariable int codigo, @RequestParam double percentual) {
        return produtoService.aumentarPreco(codigo, percentual);
    }
}
