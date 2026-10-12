package aps.loja.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(int codigo) {
        return produtoRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @Transactional
    public Produto aplicarDesconto(int codigo, double percentual) {
        Produto produto = findById(codigo);
        produto.aplicarDesconto(percentual);
        return produto;
    }

    @Transactional
    public Produto aumentarPreco(int codigo, double percentual) {
        Produto produto = findById(codigo);
        produto.aumentarPreco(percentual);
        return produto;
    }
}
