package com.example.voleimatch.domain

data class Time(
    val numero: Int,
    val jogadores: List<Jogador>
) {
    val somaNiveis: Int
        get() = jogadores.sumOf { it.nivel }

    val mediaNiveis: Double
        get () = if (jogadores.isEmpty()) 0.0 else somaNiveis.toDouble() / jogadores.size
}

data class ResultadoSorteio(
    val times: List<Time>,
    val reservas: List<Jogador>
)