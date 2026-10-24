package com.example.racha.domain

import com.example.racha.util.PLACAR_MAXIMO

enum class Lado { ESQUERDA, DIREITA }

data class Placar (
    val pontosEsquerda: Int = 0,
    val pontosDireita: Int = 0,
    val faltasEsquerda: Int = 0,
    val faltasDireita: Int = 0
) {
    fun alterarPontos(lado: Lado. delta: Int): Placar = when (lado) {
        Lado.ESQUERDA -> copy(pontosEsquerda = limitar(pontosEsquerda + delta))
        Lado.DIREITA -> copy(pontosDireita = limitar(pontosDireita + delta))
    }

    fun alterarFaltas(lado: Lado, delta: Int): Placar = when (lado) {
        Lado.ESQUERDA -> copy(faltasEsquerda = limitar(faltasEsquerda + delta))
        Lado.DIREITA -> copy(faltasDireita = limitar(faltasDireita + delta))
    }

    fun trocarLados(): Placar =
        Placar(
            pontosDireita,
            pontosEsquerda,
            faltasDireita,
            faltasEsquerda
        )

    fun limitado(): Placar = Placar(
        limitar(pontosEsquerda),
        limitar(pontosDireita),
        limitar(faltasEsquerda),
        limitar(faltasDireita)
    )

    private fun limitar(valor: Int) = valor.coerceIn(0, PLACAR_MAXIMO)
}