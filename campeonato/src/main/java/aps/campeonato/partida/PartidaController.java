package aps.campeonato.partida;

import aps.campeonato.cartao.Cartao;
import aps.campeonato.time.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public Partida create(@RequestParam int campeonatoId,
                          @RequestParam int mandanteId,
                          @RequestParam int visitanteId,
                          @RequestBody Partida partida) {
        return partidaService.create(campeonatoId, mandanteId, visitanteId, partida);
    }

    @GetMapping("/{id}")
    public Partida findById(@PathVariable int id) {
        return partidaService.findById(id);
    }

    @PutMapping("/{id}/gols")
    public Partida registrarGol(@PathVariable int id,
                                @RequestParam int golMandante,
                                @RequestParam int golVisitante) {
        return partidaService.registrarGol(id, golMandante, golVisitante);
    }

    @PostMapping("/{id}/cartoes")
    public Partida registrarCartao(@PathVariable int id,
                                   @RequestParam int jogadorId,
                                   @RequestBody Cartao cartao) {
        return partidaService.registrarCartao(id, jogadorId, cartao);
    }

    @GetMapping("/{id}/vencedor")
    public ResponseEntity<Time> buscarVencedor(@PathVariable int id) {
        Time vencedor = partidaService.buscarVencedor(id);
        return vencedor == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(vencedor);
    }

    @GetMapping("/{id}/placar")
    public String exibirPlacar(@PathVariable int id) {
        return partidaService.exibirPlacar(id);
    }
}
