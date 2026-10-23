package com.example.racha.domain

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class SorteadorTest {

    private fun jogadores(vararg niveis: Int): List<Jogador> =
        niveis.mapIndexed { i, nivel ->
            Jogador(id = (i + 1).toLong(), nome = "J${i + 1}", nivel = nivel)
        }

    @Test
    fun `monta a quantidade certa de times e reservas`() {
        val presentes = jogadores(3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3)

        val resultado = Sorteador.sortear(presentes, quantidadeTimes = 2, jogadoresPorTime = 6, random = Random(1))

        assertEquals(2, resultado.times.size)
        assertEquals(listOf(6, 6), resultado.times.map { it.jogadores.size })
        assertEquals(2, resultado.reservas.size)
    }

    @Test
    fun `nenhum jogador some nem aparece duas vezes`() {
        val presentes = jogadores(5, 4, 4, 3, 3, 3, 2, 2, 1, 1)

        val resultado = Sorteador.sortear(presentes, quantidadeTimes = 2, jogadoresPorTime = 4, random = Random(7))

        val idsSorteados = resultado.times.flatMap { it.jogadores }.map { it.id } +
                resultado.reservas.map { it.id }
        assertEquals(presentes.map { it.id }.sorted(), idsSorteados.sorted())
    }

    @Test
    fun `snake draft deixa os times com a mesma soma de niveis`() {
        val presentes = jogadores(5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2)

        val resultado = Sorteador.sortear(presentes, quantidadeTimes = 3, jogadoresPorTime = 4, random = Random(3))

        assertEquals(listOf(14, 14, 14), resultado.times.map { it.somaNiveis })
    }

    @Test
    fun `falha quando ha menos presentes do que vagas`() {
        val presentes = jogadores(3, 3, 3)

        assertThrows(IllegalArgumentException::class.java) {
            Sorteador.sortear(presentes, quantidadeTimes = 2, jogadoresPorTime = 3)
        }
    }

    @Test
    fun `mesma semente gera o mesmo resultado`() {
        val presentes = jogadores(5, 4, 4, 3, 3, 3, 2, 2)

        val a = Sorteador.sortear(presentes, 2, 3, Random(99))
        val b = Sorteador.sortear(presentes, 2, 3, Random(99))

        assertEquals(a, b)
    }

    @Test
    fun `sorteios diferentes geram formacoes diferentes`() {
        val presentes = jogadores(5, 4, 4, 3, 3, 3, 2, 2)

        val formacoes = (1..20).map { semente ->
            Sorteador.sortear(presentes, 2, 3, Random(semente))
                .times
                .map { time -> time.jogadores.map { j -> j.id }.toSet() }
                .toSet()
        }.toSet()

        assertTrue(formacoes.size > 1)
    }

    @Test
    fun `reservas nao dependem do nivel`() {
        // J1 tem o maior nivel e J8 o menor; os dois devem ter chance de ficar na reserva
        val presentes = jogadores(5, 4, 4, 3, 3, 3, 2, 1)

        val reservasPorSorteio = (1..50).map { semente ->
            Sorteador.sortear(presentes, 2, 3, Random(semente)).reservas.map { it.id }
        }

        assertTrue(reservasPorSorteio.any { 1L in it })
        assertTrue(reservasPorSorteio.any { 8L in it })
    }
}