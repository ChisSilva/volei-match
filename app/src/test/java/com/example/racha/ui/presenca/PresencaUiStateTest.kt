package com.example.racha.ui.presenca

import com.example.racha.domain.Jogador
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PresencaUiStateTest {

    private val jogadores = (1L..8L).map { Jogador(id = it, nome = "J$it", nivel = 3) }

    @Test
    fun `conta apenas presentes que ainda existem`() {
        val estado = PresencaUiState(
            jogadores = jogadores.take(3),
            presentesIds = setOf(1L, 2L, 99L)
        )

        assertEquals(2, estado.totalPresentes)
    }

    @Test
    fun `nao pode sortear com presentes insuficientes`() {
        val estado = PresencaUiState(
            jogadores = jogadores,
            presentesIds = setOf(1L, 2L, 3L),
            quantidadeTimes = 2,
            jogadoresPorTime = 3
        )

        assertFalse(estado.podeSortear)
        assertEquals(3, estado.faltam)
    }

    @Test
    fun `excedentes viram reservas`() {
        val estado = PresencaUiState(
            jogadores = jogadores,
            presentesIds = jogadores.map { it.id }.toSet(),
            quantidadeTimes = 2,
            jogadoresPorTime = 3
        )

        assertTrue(estado.podeSortear)
        assertEquals(2, estado.reservas)
    }
}