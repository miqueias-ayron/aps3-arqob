package aps.campeonato.partida;

import aps.campeonato.campeonato.Campeonato;
import aps.campeonato.campeonato.CampeonatoService;
import aps.campeonato.cartao.Cartao;
import aps.campeonato.jogador.JogadorRepository;
import aps.campeonato.time.Time;
import aps.campeonato.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private CampeonatoService campeonatoService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Transactional
    public Partida create(int campeonatoId, int mandanteId, int visitanteId, Partida partida) {
        Campeonato campeonato = campeonatoService.findById(campeonatoId);
        partida.registrarTimes(timeService.findById(mandanteId), timeService.findById(visitanteId));
        campeonato.adicionarPartida(partida);
        return partidaRepository.save(partida);
    }

    public Partida findById(int id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    @Transactional
    public Partida registrarGol(int id, int golMandante, int golVisitante) {
        Partida partida = findById(id);
        partida.registrarGol(golMandante, golVisitante);
        return partida;
    }

    @Transactional
    public Partida registrarCartao(int id, int jogadorId, Cartao cartao) {
        Partida partida = findById(id);
        cartao.setJogador(jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado")));
        partida.registrarCartao(cartao);
        return partida;
    }

    @Transactional(readOnly = true)
    public Time buscarVencedor(int id) {
        return findById(id).buscarVencedor();
    }

    @Transactional(readOnly = true)
    public String exibirPlacar(int id) {
        return findById(id).exibirPlacar();
    }
}
