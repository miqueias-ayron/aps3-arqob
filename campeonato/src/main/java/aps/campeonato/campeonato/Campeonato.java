package aps.campeonato.campeonato;

import aps.campeonato.partida.Partida;
import aps.campeonato.time.Time;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

@Entity
public class Campeonato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Min(1900)
    private int ano;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "id_campeonato"),
            inverseJoinColumns = @JoinColumn(name = "id_time")
    )
    private List<Time> times = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL)
    private List<Partida> partidas = new ArrayList<>();

    public void adicionarTime(Time time) {
        if (buscarTime(time.getNome()) != null) {
            throw new IllegalArgumentException("Time já cadastrado no campeonato: " + time.getNome());
        }
        times.add(time);
    }

    public void adicionarPartida(Partida partida) {
        if (!times.contains(partida.getTimeMandante()) || !times.contains(partida.getTimeVisitante())) {
            throw new IllegalArgumentException("Os dois times da partida devem pertencer ao campeonato");
        }
        partida.setCampeonato(this);
        partidas.add(partida);
    }

    public List<Time> listarTimes() {
        return Collections.unmodifiableList(times);
    }

    public List<Partida> listarPartidas() {
        return Collections.unmodifiableList(partidas);
    }

    public Time buscarTime(String nome) {
        return times.stream()
                .filter(time -> time.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public List<Classificacao> exibirClassificacao() {
        Map<Time, Classificacao> tabela = new LinkedHashMap<>();
        times.forEach(time -> tabela.put(time, Classificacao.inicial(time)));

        for (Partida partida : partidas) {
            if (!partida.isFinalizada()) {
                continue;
            }
            Time mandante = partida.getTimeMandante();
            Time visitante = partida.getTimeVisitante();
            tabela.computeIfPresent(mandante, (time, linha) ->
                    linha.registrarResultado(partida.getGolsMandante(), partida.getGolsVisitante()));
            tabela.computeIfPresent(visitante, (time, linha) ->
                    linha.registrarResultado(partida.getGolsVisitante(), partida.getGolsMandante()));
        }

        return tabela.values().stream()
                .sorted(Comparator.comparingInt(Classificacao::pontos)
                        .thenComparingInt(Classificacao::vitorias)
                        .thenComparingInt(Classificacao::saldoGols)
                        .thenComparingInt(Classificacao::golsPro)
                        .reversed())
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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}
