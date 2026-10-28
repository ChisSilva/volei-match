package com.example.racha.ui.jogadores

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.racha.domain.Jogador
import com.example.racha.util.NIVEL_MAXIMO
import com.example.racha.util.NIVEL_MINIMO

@Composable
fun JogadorFormDialog(
    jogador: Jogador,
    onConfirmar: (nome: String, nivel: Int) -> Unit,
    onCancelar: () -> Unit
) {
    var nome by remember { mutableStateOf(jogador.nome) }
    var nivel by remember { mutableIntStateOf(jogador.nivel) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (jogador.id == 0L) "Novo jogador" else "Editar jogador") },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                Spacer(Modifier.height(16.dp))
                Text("Nível")
                SeletorDeNivel(nivel = nivel, onNivelChange = { nivel = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmar(nome, nivel) },
                enabled = nome.isNotBlank()
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}

@Composable
private fun SeletorDeNivel(nivel: Int, onNivelChange: (Int) -> Unit) {
    Row {
        for (estrela in NIVEL_MINIMO..NIVEL_MAXIMO) {
            IconButton(
                onClick = { onNivelChange(estrela) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Nível $estrela",
                    tint = if (estrela <= nivel) Color(0xFFFFB300)
                    else MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

class JogadorFormDialog {
}