package com.example.racha.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.racha.util.NIVEL_MAXIMO
import com.example.racha.util.NIVEL_MINIMO

private val CorEstrela = Color(0xFFFFB300)

@Composable
fun Estrelas(
    nivel: Int,
    modifier: Modifier = Modifier,
    tamanho: Dp = 14.dp
) {
    Row(
        modifier = modifier.semantics {
            contentDescription = "Nível $nivel de $NIVEL_MAXIMO"
        }
    ) {
        for (posicao in NIVEL_MINIMO..NIVEL_MAXIMO) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(tamanho),
                tint = if (posicao <= nivel) CorEstrela
                else MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun AvatarInicial(
    nome: String,
    destacado: Boolean,
    modifier: Modifier = Modifier,
    tamanho: Dp = 44.dp
) {
    val fundo = if (destacado) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceVariant
    val letra = if (destacado) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(fundo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nome.trim().take(1).uppercase(),
            color = letra,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}