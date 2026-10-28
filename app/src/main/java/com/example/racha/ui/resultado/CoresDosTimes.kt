package com.example.racha.ui.resultado

import androidx.compose.ui.graphics.Color

private val CoresDosTimes = listOf(
    Color(0xFFD9480F), // laranja
    Color(0xFF1C7ED6), // azul
    Color(0xFF2B8A3E), // verde
    Color(0xFF7048E8), // violeta
    Color(0xFFC2255C), // magenta
    Color(0xFF0C8599)  // turquesa
)

fun corDoTime(numero: Int): Color =
    CoresDosTimes[(numero - 1).mod(CoresDosTimes.size)]