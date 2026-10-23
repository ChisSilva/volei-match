package com.example.racha.util

import com.example.racha.domain.Jogador
import com.example.racha.domain.ResultadoSorteio
import com.example.racha.domain.Time
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormatadorWhatsAppTest {

    private val ana = Jogador(id = 1, nome = "Ana", nivel = 5)
    private val beto = Jogador(id = 2, nome = "Beto", nivel = 3)
    private val caio = Jogador(id = 3, nome = "Caio", nivel = 2)

    @Test
    fun `mostra cabecalho do time com soma e media`() {
        val resultado = ResultadoSorteio(
            times = listOf(Time(numero = 1, jogadores = listOf(ana, beto))),
            reservas = emptyList()
        )

        val texto = FormatadorWhatsApp.formatar(resultado)

        assertTrue(texto.contains("*Time 1*"))
        assertTrue(texto.contains("8⭐"))
        assertTrue(texto.contains("média 4,0"))
    }

    @Test
    fun `lista os jogadores com suas estrelas`() {
        val resultado = ResultadoSorteio(
            times = listOf(Time(numero = 1, jogadores = listOf(ana, beto))),
            reservas = emptyList()
        )

        val texto = FormatadorWhatsApp.formatar(resultado)

        assertTrue(texto.contains("• Ana ⭐⭐⭐⭐⭐"))
        assertTrue(texto.contains("• Beto ⭐⭐⭐"))
    }

    @Test
    fun `nao mostra reservas quando nao ha`() {
        val resultado = ResultadoSorteio(
            times = listOf(Time(numero = 1, jogadores = listOf(ana))),
            reservas = emptyList()
        )

        assertFalse(FormatadorWhatsApp.formatar(resultado).contains("Reservas"))
    }

    @Test
    fun `mostra reservas quando ha`() {
        val resultado = ResultadoSorteio(
            times = listOf(Time(numero = 1, jogadores = listOf(ana))),
            reservas = listOf(caio)
        )

        val texto = FormatadorWhatsApp.formatar(resultado)

        assertTrue(texto.contains("*Reservas*"))
        assertTrue(texto.contains("• Caio ⭐⭐"))
    }
}