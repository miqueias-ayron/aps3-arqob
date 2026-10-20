package aps.campeonato.campeonato;

import jakarta.validation.Valid;
import aps.campeonato.partida.Partida;
import aps.campeonato.time.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campeonatos")
public class CampeonatoController {

    @Autowired
    private CampeonatoService campeonatoService;

    @PostMapping
    public Campeonato create(@Valid @RequestBody Campeonato campeonato) {
        return campeonatoService.create(campeonato);
    }

    @GetMapping
    public List<Campeonato> findAll() {
        return campeonatoService.findAll();
    }

    @GetMapping("/{id}")
    public Campeonato findById(@PathVariable int id) {
        return campeonatoService.findById(id);
    }

    @PostMapping("/{id}/times/{timeId}")
    public List<Time> adicionarTime(@PathVariable int id, @PathVariable int timeId) {
        return campeonatoService.adicionarTime(id, timeId);
    }

    @GetMapping("/{id}/times")
    public List<Time> listarTimes(@PathVariable int id) {
        return campeonatoService.listarTimes(id);
    }

    @GetMapping("/{id}/times/busca")
    public Time buscarTime(@PathVariable int id, @RequestParam String nome) {
        return campeonatoService.buscarTime(id, nome);
    }

    @GetMapping("/{id}/partidas")
    public List<Partida> listarPartidas(@PathVariable int id) {
        return campeonatoService.listarPartidas(id);
    }

    @GetMapping("/{id}/classificacao")
    public List<Classificacao> exibirClassificacao(@PathVariable int id) {
        return campeonatoService.exibirClassificacao(id);
    }
}
