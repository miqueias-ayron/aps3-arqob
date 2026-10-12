package aps.campeonato.partida;

import aps.campeonato.campeonato.Campeonato;
import aps.campeonato.cartao.Cartao;
import aps.campeonato.time.Time;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_time_mandante")
    private Time timeMandante;

    @ManyToOne
    @JoinColumn(name = "id_time_visitante")
    private Time timeVisitante;

    // null enquanto a partida não tiver placar registrado
    private Integer golsMandante;
    private Integer golsVisitante;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cartao> cartoes = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_campeonato")
    private Campeonato campeonato;

    public void registrarTimes(Time timeMandante, Time timeVisitante) {
        if (timeMandante.equals(timeVisitante)) {
            throw new IllegalArgumentException("Mandante e visitante devem ser times diferentes");
        }
        this.timeMandante = timeMandante;
        this.timeVisitante = timeVisitante;
    }

    public void registrarGol(int golMandante, int golVisitante) {
        if (golMandante < 0 || golVisitante < 0) {
            throw new IllegalArgumentException("Quantidade de gols não pode ser negativa");
        }
        this.golsMandante = golMandante;
        this.golsVisitante = golVisitante;
    }

    public void registrarCartao(Cartao cartao) {
        Time timeJogador = cartao.getJogador().getTime();
        if (!timeMandante.equals(timeJogador) && !timeVisitante.equals(timeJogador)) {
            throw new IllegalArgumentException("Jogador não pertence a nenhum dos times da partida");
        }
        cartao.setPartida(this);
        cartoes.add(cartao);
    }

    /**
     * Retorna o time vencedor, ou null em caso de empate ou partida sem placar.
     */
    public Time buscarVencedor() {
        if (!isFinalizada() || golsMandante.equals(golsVisitante)) {
            return null;
        }
        return golsMandante > golsVisitante ? timeMandante : timeVisitante;
    }

    public String exibirPlacar() {
        String placarMandante = golsMandante == null ? "-" : golsMandante.toString();
        String placarVisitante = golsVisitante == null ? "-" : golsVisitante.toString();
        return "%s %s x %s %s".formatted(
                timeMandante.getNome(), placarMandante, placarVisitante, timeVisitante.getNome());
    }

    @JsonIgnore
    public boolean isFinalizada() {
        return golsMandante != null && golsVisitante != null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Time getTimeMandante() {
        return timeMandante;
    }

    public Time getTimeVisitante() {
        return timeVisitante;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }
}
