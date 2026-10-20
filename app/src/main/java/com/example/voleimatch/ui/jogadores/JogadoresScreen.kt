package com.example.voleimatch.ui.jogadores
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voleimatch.domain.Jogador
import com.example.voleimatch.util.NIVEL_PADRAO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JogadoresScreen(
    viewModel: JogadoresViewModel = viewModel(factory = JogadoresViewModel.Factory)
) {
    val jogadores by viewModel.jogadores.collectAsStateWithLifecycle()

    var formulario by remember { mutableStateOf<Jogador?>(null) }
    var jogadorParaExcluir by remember { mutableStateOf<Jogador?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Jogadores") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { formulario = Jogador(nome = "", nivel = NIVEL_PADRAO) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar jogador")
            }
        }
    ) { padding ->
        if (jogadores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum jogador cadastrado.\nToque no + para adicionar.",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(jogadores, key = { it.id }) { jogador ->
                    JogadorItem(
                        jogador = jogador,
                        onEditar = { formulario = jogador },
                        onExcluir = { jogadorParaExcluir = jogador }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    formulario?.let { jogador ->
        JogadorFormDialog(
            jogador = jogador,
            onConfirmar = { nome, nivel ->
                viewModel.salvar(jogador.id, nome, nivel)
                formulario = null
            },
            onCancelar = { formulario = null }
        )
    }

    jogadorParaExcluir?.let { jogador ->
        AlertDialog(
            onDismissRequest = { jogadorParaExcluir = null },
            title = { Text("Excluir jogador") },
            text = { Text("Deseja remover ${jogador.nome}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.remover(jogador)
                    jogadorParaExcluir = null
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { jogadorParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun JogadorItem(
    jogador: Jogador,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    ListItem(
        headlineContent = { Text(jogador.nome) },
        supportingContent = { Text("⭐".repeat(jogador.nivel)) },
        trailingContent = {
            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar ${jogador.nome}")
                }
                IconButton(onClick = onExcluir) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir ${jogador.nome}")
                }
            }
        }
    )
}