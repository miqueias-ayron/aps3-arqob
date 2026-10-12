package aps.loja.produto;

import aps.loja.categoria.Categoria;
import aps.loja.loja.Loja;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Produto {
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_loja")
    private Loja loja;

    public void aplicarDesconto(double percentual) {
        if (percentual <= 0 || percentual > 100) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
        preco = ajustarPreco(-percentual);
    }

    public void aumentarPreco(double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException("Percentual de aumento deve ser positivo");
        }
        preco = ajustarPreco(percentual);
    }

    private BigDecimal ajustarPreco(double percentual) {
        BigDecimal fator = BigDecimal.ONE.add(BigDecimal.valueOf(percentual).divide(CEM));
        return preco.multiply(fator).setScale(2, RoundingMode.HALF_UP);
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.signum() < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.preco = preco.setScale(2, RoundingMode.HALF_UP);
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }
}
