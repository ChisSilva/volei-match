package com.example.racha.util

import com.example.racha.domain.ResultadoSorteio
import java.util.Locale

object FormatadorWhatsApp {

    private val ptBr: Locale = Locale.forLanguageTag("pt-BR")

    fun formatarMedia(valor: Double): String = "%.1f".format(ptBr, valor)

    fun formatar(resultado: ResultadoSorteio): String = buildString {
        appendLine("📋 *Times de hoje*")

        resultado.times.forEach { time ->
            appendLine()
            appendLine(
                "*Time ${time.numero}* (${time.somaNiveis}⭐ | média ${formatarMedia(time.mediaNiveis)})"
            )
            time.jogadores.forEach { appendLine("• ${it.nome} ${"⭐".repeat(it.nivel)}") }
        }

        if (resultado.reservas.isNotEmpty()) {
            appendLine()
            appendLine("🪑 *Reservas*")
            resultado.reservas.forEach { appendLine("• ${it.nome} ${"⭐".repeat(it.nivel)}") }
        }
    }.trimEnd()
}