package aps.campeonato.time;

import aps.campeonato.jogador.Jogador;
import aps.campeonato.jogador.JogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TimeService {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    public Time create(Time time) {
        return timeRepository.save(time);
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time findById(int id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time não encontrado"));
    }

    @Transactional
    public Jogador adicionarJogador(int id, Jogador jogador) {
        Time time = findById(id);
        time.adicionarJogador(jogador);
        return jogadorRepository.save(jogador);
    }

    @Transactional
    public void removerJogador(int id, int jogadorId) {
        Time time = findById(id);
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
        time.removerJogador(jogador);
    }

    @Transactional(readOnly = true)
    public List<Jogador> listarJogadores(int id) {
        return List.copyOf(findById(id).listarJogadores());
    }

    @Transactional(readOnly = true)
    public double mediaIdadeJogadores(int id) {
        return findById(id).mediaIdadeJogadores();
    }
}
