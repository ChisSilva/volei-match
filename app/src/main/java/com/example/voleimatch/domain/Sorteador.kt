package com.example.voleimatch.domain

import kotlin.random.Random

object Sorteador {

    fun sortear(
        presentes: List<Jogador>,
        quantidadeTimes: Int,
        jogadoresPorTime: Int,
        random: Random = Random.Default
    ): ResultadoSorteio {
        require(quantidadeTimes > 0) { "A quantidade de times deve ser maior que zero." }
        require(jogadoresPorTime > 0) { "Jogadores por time deve ser maior que zero." }

        val vagas = quantidadeTimes * jogadoresPorTime
        require(presentes.size >= vagas) { "Presentes insuficientes: são necessários $vagas, mas há ${presentes.size}." }

        // Reservas definidos por sorteio aleatório
        // é feito um embaralhamento da lista de jogadores presentes
        // os titulares são preenchidos com a quantidade necessária para formar os times
        // o que sobrar, são reservas
        val embaralhados = presentes.shuffled(random)
        val titulares = embaralhados.take(vagas)
        val reservas = embaralhados.drop(vagas)

        // Preenchimento dos potes por nível (do maior para o menor), com cada um embaralhado
        val ordemDeEscolha = titulares
            .groupBy { it.nivel }
            .entries
            .sortedByDescending { it.key }
            .flatMap { pote -> pote.value.shuffled(random) }

        // Algoritmo Snake draft (serpentina)
        val listas = List(quantidadeTimes) { mutableListOf<Jogador>() }
        ordemDeEscolha.forEachIndexed { indice, jogador ->
            val rodada = indice / quantidadeTimes
            val posicao = indice % quantidadeTimes
            val time = if (rodada % 2 == 0) posicao else quantidadeTimes -1
            listas[time].add(jogador)
        }

        // Embaralha a ordem dos times e numera
        val times = listas
            .shuffled(random)
            .mapIndexed { i, jogadores -> Time(numero = i + 1, jogadores = jogadores) }

        return ResultadoSorteio(times = times, reservas = reservas)
    }
}