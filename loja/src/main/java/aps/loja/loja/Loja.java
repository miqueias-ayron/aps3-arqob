package aps.loja.loja;

import aps.loja.categoria.Categoria;
import aps.loja.cliente.Cliente;
import aps.loja.produto.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Loja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL)
    private List<Cliente> clientes = new ArrayList<>();

    public void adicionarProduto(Produto produto) {
        if (produtos.contains(produto)) {
            throw new IllegalArgumentException("Produto já cadastrado na loja: " + produto.getNome());
        }
        produto.setLoja(this);
        produtos.add(produto);
    }

    public void adicionarCliente(Cliente cliente) {
        if (buscarCliente(cliente.getCpf()) != null) {
            throw new IllegalArgumentException("Cliente já cadastrado na loja: " + cliente.getCpf());
        }
        cliente.setLoja(this);
        clientes.add(cliente);
    }

    public Produto buscarProduto(int codigo) {
        return produtos.stream()
                .filter(produto -> produto.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    public Cliente buscarCliente(String cpf) {
        return clientes.stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    public List<Produto> listarProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public List<Produto> listarProdutosPorCategoria(Categoria categoria) {
        return produtos.stream()
                .filter(produto -> categoria.equals(produto.getCategoria()))
                .toList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
