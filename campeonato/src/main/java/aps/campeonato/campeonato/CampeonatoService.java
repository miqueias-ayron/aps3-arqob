package aps.campeonato.campeonato;

import aps.campeonato.partida.Partida;
import aps.campeonato.time.Time;
import aps.campeonato.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CampeonatoService {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private TimeService timeService;

    public Campeonato create(Campeonato campeonato) {
        return campeonatoRepository.save(campeonato);
    }

    public List<Campeonato> findAll() {
        return campeonatoRepository.findAll();
    }

    public Campeonato findById(int id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campeonato não encontrado"));
    }

    @Transactional
    public List<Time> adicionarTime(int id, int timeId) {
        Campeonato campeonato = findById(id);
        campeonato.adicionarTime(timeService.findById(timeId));
        return List.copyOf(campeonato.listarTimes());
    }

    @Transactional(readOnly = true)
    public List<Time> listarTimes(int id) {
        return List.copyOf(findById(id).listarTimes());
    }

    @Transactional(readOnly = true)
    public Time buscarTime(int id, String nome) {
        Time time = findById(id).buscarTime(nome);
        if (time == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Time não encontrado no campeonato");
        }
        return time;
    }

    @Transactional(readOnly = true)
    public List<Partida> listarPartidas(int id) {
        return List.copyOf(findById(id).listarPartidas());
    }

    @Transactional(readOnly = true)
    public List<Classificacao> exibirClassificacao(int id) {
        return findById(id).exibirClassificacao();
    }
}
