package com.example.racha.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class OrdenacaoTest {

    private val ana = Jogador(id = 1, nome = "ana", nivel = 3)
    private val bia = Jogador(id = 2, nome = "Bia", nivel = 5)
    private val caio = Jogador(id = 3, nome = "Caio", nivel = 3)
    private val zeca = Jogador(id = 4, nome = "Zeca", nivel = 1)

    private val embaralhados = listOf(zeca, bia, caio, ana)

    private fun nomes(lista: List<Jogador>) = lista.map { it.nome }

    @Test
    fun `nome crescente ignora maiusculas e minusculas`() {
        val resultado = Ordenacao.NOME_CRESCENTE.ordenar(embaralhados)

        assertEquals(listOf("ana", "Bia", "Caio", "Zeca"), nomes(resultado))
    }

    @Test
    fun `nome decrescente inverte a ordem`() {
        val resultado = Ordenacao.NOME_DECRESCENTE.ordenar(embaralhados)

        assertEquals(listOf("Zeca", "Caio", "Bia", "ana"), nomes(resultado))
    }

    @Test
    fun `nivel decrescente desempata por nome`() {
        val resultado = Ordenacao.NIVEL_DECRESCENTE.ordenar(embaralhados)

        assertEquals(listOf("Bia", "ana", "Caio", "Zeca"), nomes(resultado))
    }

    @Test
    fun `nivel crescente desempata por nome`() {
        val resultado = Ordenacao.NIVEL_CRESCENTE.ordenar(embaralhados)

        assertEquals(listOf("Zeca", "ana", "Caio", "Bia"), nomes(resultado))
    }

    @Test
    fun `nomes com acento ficam na posicao da letra base`() {
        val erica = Jogador(id = 5, nome = "Érica", nivel = 2)
        val fabio = Jogador(id = 6, nome = "Fábio", nivel = 2)

        val resultado = Ordenacao.NOME_CRESCENTE.ordenar(listOf(fabio, erica))

        assertEquals(listOf("Érica", "Fábio"), nomes(resultado))
    }

    @Test
    fun `ordenar nao altera a lista original`() {
        val original = listOf(zeca, ana)

        Ordenacao.NOME_CRESCENTE.ordenar(original)

        assertEquals(listOf("Zeca", "ana"), nomes(original))
    }
}