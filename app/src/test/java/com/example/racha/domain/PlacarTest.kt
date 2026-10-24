package com.example.racha.domain

import com.example.racha.util.PLACAR_MAXIMO
import org.junit.Assert.assertEquals
import org.junit.Test

class PlacarTest {

    @Test
    fun `soma pontos de cada lado separadamente`() {
        val placar = Placar()
            .alterarPontos(Lado.ESQUERDA, 1)
            .alterarPontos(Lado.ESQUERDA, 1)
            .alterarPontos(Lado.DIREITA, 1)

        assertEquals(2, placar.pontosEsquerda)
        assertEquals(1, placar.pontosDireita)
    }

    @Test
    fun `pontos nao ficam abaixo de zero`() {
        val placar = Placar().alterarPontos(Lado.ESQUERDA, -1)

        assertEquals(0, placar.pontosEsquerda)
    }

    @Test
    fun `faltas nao ficam abaixo de zero`() {
        val placar = Placar().alterarFaltas(Lado.DIREITA, -1)

        assertEquals(0, placar.faltasDireita)
    }

    @Test
    fun `valores nao passam do maximo`() {
        val placar = Placar(pontosEsquerda = PLACAR_MAXIMO).alterarPontos(Lado.ESQUERDA, 1)

        assertEquals(PLACAR_MAXIMO, placar.pontosEsquerda)
    }

    @Test
    fun `trocar de lado inverte pontos e faltas`() {
        val placar = Placar(
            pontosEsquerda = 10,
            pontosDireita = 7,
            faltasEsquerda = 3,
            faltasDireita = 1
        ).trocarLados()

        assertEquals(Placar(7, 10, 1, 3), placar)
    }

    @Test
    fun `limitado corrige valores fora da faixa`() {
        val placar = Placar(
            pontosEsquerda = -5,
            pontosDireita = 500,
            faltasEsquerda = 0,
            faltasDireita = 12
        ).limitado()

        assertEquals(Placar(0, PLACAR_MAXIMO, 0, 12), placar)
    }
}