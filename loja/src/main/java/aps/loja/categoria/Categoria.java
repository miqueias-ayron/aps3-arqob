package aps.loja.categoria;

import aps.loja.produto.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos = new ArrayList<>();

    public void adicionarProduto(Produto produto) {
        if (produtos.contains(produto)) {
            throw new IllegalArgumentException("Produto já pertence à categoria: " + produto.getNome());
        }
        // Um produto pertence a uma única categoria: sai da anterior antes de entrar nesta
        Categoria anterior = produto.getCategoria();
        if (anterior != null) {
            anterior.produtos.remove(produto);
        }
        produto.setCategoria(this);
        produtos.add(produto);
    }

    public List<Produto> listarProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public BigDecimal calcularPrecoMedio() {
        if (produtos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal soma = produtos.stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return soma.divide(BigDecimal.valueOf(produtos.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Retorna o produto de maior preço, ou null se a categoria não tiver produtos.
     */
    public Produto buscarProdutoMaisCaro() {
        return produtos.stream()
                .max(Comparator.comparing(Produto::getPreco))
                .orElse(null);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
