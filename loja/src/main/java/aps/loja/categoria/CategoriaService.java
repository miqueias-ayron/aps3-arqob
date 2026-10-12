package aps.loja.categoria;

import aps.loja.produto.Produto;
import aps.loja.produto.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoService produtoService;

    public Categoria create(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(int codigo) {
        return categoriaRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    @Transactional
    public List<Produto> adicionarProduto(int codigo, int produtoCodigo) {
        Categoria categoria = findById(codigo);
        categoria.adicionarProduto(produtoService.findById(produtoCodigo));
        return List.copyOf(categoria.listarProdutos());
    }

    @Transactional(readOnly = true)
    public List<Produto> listarProdutos(int codigo) {
        return List.copyOf(findById(codigo).listarProdutos());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularPrecoMedio(int codigo) {
        return findById(codigo).calcularPrecoMedio();
    }

    @Transactional(readOnly = true)
    public Produto buscarProdutoMaisCaro(int codigo) {
        return findById(codigo).buscarProdutoMaisCaro();
    }
}
