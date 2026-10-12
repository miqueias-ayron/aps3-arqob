package aps.loja.loja;

import aps.loja.categoria.Categoria;
import aps.loja.categoria.CategoriaService;
import aps.loja.cliente.Cliente;
import aps.loja.cliente.ClienteRepository;
import aps.loja.produto.Produto;
import aps.loja.produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LojaService {

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriaService categoriaService;

    public Loja create(Loja loja) {
        return lojaRepository.save(loja);
    }

    public List<Loja> findAll() {
        return lojaRepository.findAll();
    }

    public Loja findById(int id) {
        return lojaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loja não encontrada"));
    }

    @Transactional
    public Produto adicionarProduto(int id, int categoriaId, Produto produto) {
        Loja loja = findById(id);
        categoriaService.findById(categoriaId).adicionarProduto(produto);
        loja.adicionarProduto(produto);
        return produtoRepository.save(produto);
    }

    @Transactional
    public Cliente adicionarCliente(int id, Cliente cliente) {
        Loja loja = findById(id);
        loja.adicionarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Produto buscarProduto(int id, int codigo) {
        Produto produto = findById(id).buscarProduto(codigo);
        if (produto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado na loja");
        }
        return produto;
    }

    @Transactional(readOnly = true)
    public Cliente buscarCliente(int id, String cpf) {
        Cliente cliente = findById(id).buscarCliente(cpf);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado na loja");
        }
        return cliente;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarProdutos(int id) {
        return List.copyOf(findById(id).listarProdutos());
    }

    @Transactional(readOnly = true)
    public List<Produto> listarProdutosPorCategoria(int id, int categoriaId) {
        Categoria categoria = categoriaService.findById(categoriaId);
        return findById(id).listarProdutosPorCategoria(categoria);
    }
}
