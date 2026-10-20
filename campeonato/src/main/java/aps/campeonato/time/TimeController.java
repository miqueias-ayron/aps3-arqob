package aps.campeonato.time;

import jakarta.validation.Valid;
import aps.campeonato.jogador.Jogador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    @Autowired
    private TimeService timeService;

    @PostMapping
    public Time create(@Valid @RequestBody Time time) {
        return timeService.create(time);
    }

    @GetMapping
    public List<Time> findAll() {
        return timeService.findAll();
    }

    @GetMapping("/{id}")
    public Time findById(@PathVariable int id) {
        return timeService.findById(id);
    }

    @PostMapping("/{id}/jogadores")
    public Jogador adicionarJogador(@PathVariable int id, @Valid @RequestBody Jogador jogador) {
        return timeService.adicionarJogador(id, jogador);
    }

    @DeleteMapping("/{id}/jogadores/{jogadorId}")
    public void removerJogador(@PathVariable int id, @PathVariable int jogadorId) {
        timeService.removerJogador(id, jogadorId);
    }

    @GetMapping("/{id}/jogadores")
    public List<Jogador> listarJogadores(@PathVariable int id) {
        return timeService.listarJogadores(id);
    }

    @GetMapping("/{id}/media-idade")
    public double mediaIdadeJogadores(@PathVariable int id) {
        return timeService.mediaIdadeJogadores(id);
    }
}
