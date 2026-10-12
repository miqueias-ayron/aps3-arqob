package aps.campeonato.campeonato;

import aps.campeonato.time.Time;

public record Classificacao(
        String time,
        int pontos,
        int jogos,
        int vitorias,
        int empates,
        int derrotas,
        int golsPro,
        int golsContra,
        int saldoGols
) {
    public static final int PONTOS_VITORIA = 3;
    public static final int PONTOS_EMPATE = 1;
    public static final int PONTOS_DERROTA = 0;

    static Classificacao inicial(Time time) {
        return new Classificacao(time.getNome(), 0, 0, 0, 0, 0, 0, 0, 0);
    }

    Classificacao registrarResultado(int golsFeitos, int golsSofridos) {
        boolean vitoria = golsFeitos > golsSofridos;
        boolean empate = golsFeitos == golsSofridos;
        int pontosGanhos = vitoria ? PONTOS_VITORIA : empate ? PONTOS_EMPATE : PONTOS_DERROTA;

        return new Classificacao(
                time,
                pontos + pontosGanhos,
                jogos + 1,
                vitorias + (vitoria ? 1 : 0),
                empates + (empate ? 1 : 0),
                derrotas + (!vitoria && !empate ? 1 : 0),
                golsPro + golsFeitos,
                golsContra + golsSofridos,
                saldoGols + golsFeitos - golsSofridos
        );
    }
}
