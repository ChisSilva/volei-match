package com.example.racha.domain

data class Sorteio(
    val presentes: List<Jogador>,
    val quantidadeTimes: Int,
    val jogadoresPorTime: Int,
    val resultado: ResultadoSorteio
)