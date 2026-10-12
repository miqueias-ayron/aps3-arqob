package aps.campeonato.time;

import aps.campeonato.jogador.Jogador;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "tb_time")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;
    private String cidade;

    @JsonIgnore
    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogador> jogadores = new ArrayList<>();

    public void adicionarJogador(Jogador jogador) {
        boolean numeroEmUso = jogadores.stream()
                .anyMatch(j -> j.getNumero() == jogador.getNumero());
        if (numeroEmUso) {
            throw new IllegalArgumentException("Número já utilizado no time: " + jogador.getNumero());
        }
        jogador.setTime(this);
        jogadores.add(jogador);
    }

    public void removerJogador(Jogador jogador) {
        if (!jogadores.remove(jogador)) {
            throw new IllegalArgumentException("Jogador não pertence ao time");
        }
        jogador.setTime(null);
    }

    public List<Jogador> listarJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    public double mediaIdadeJogadores() {
        return jogadores.stream()
                .mapToInt(Jogador::getIdade)
                .average()
                .orElse(0);
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
