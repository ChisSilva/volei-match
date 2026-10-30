package com.example.racha.ui.tutorial

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PassosTutorialTest {

    @Test
    fun `tem os 4 passos previstos`() {
        assertEquals(4, PassosDoTutorial.size)
    }

    @Test
    fun `todo passo tem titulo e texto`() {
        assertTrue(PassosDoTutorial.all { it.titulo.isNotBlank() && it.texto.isNotBlank() })
    }

    @Test
    fun `os textos nao usam termos tecnicos`() {
        assertTrue(
            PassosDoTutorial.none { (it.titulo + it.texto).contains("snake", ignoreCase = true) }
        )
    }
}