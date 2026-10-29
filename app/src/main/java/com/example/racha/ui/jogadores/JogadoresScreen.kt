package com.example.racha.ui.jogadores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha.R
import com.example.racha.domain.Jogador
import com.example.racha.domain.Ordenacao
import com.example.racha.ui.componentes.AvatarInicial
import com.example.racha.ui.componentes.Estrelas
import com.example.racha.util.NIVEL_PADRAO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JogadoresScreen(
    onVoltar: () -> Unit,
    viewModel: JogadoresViewModel = viewModel(factory = JogadoresViewModel.Factory)
) {
    val jogadores by viewModel.jogadores.collectAsStateWithLifecycle()
    val ordenacao by viewModel.ordenacao.collectAsStateWithLifecycle()

    var formulario by remember { mutableStateOf<Jogador?>(null) }
    var jogadorParaExcluir by remember { mutableStateOf<Jogador?>(null) }
    var menuOrdenacaoAberto by remember { mutableStateOf(false) }

    val abrirNovo = { formulario = Jogador(nome = "", nivel = NIVEL_PADRAO) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jogadores", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuOrdenacaoAberto = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_ordenar),
                                contentDescription = "Ordenar lista"
                            )
                        }
                        DropdownMenu(
                            expanded = menuOrdenacaoAberto,
                            onDismissRequest = { menuOrdenacaoAberto = false }
                        ) {
                            Ordenacao.entries.forEach { opcao ->
                                DropdownMenuItem(
                                    text = { Text(opcao.rotulo) },
                                    onClick = {
                                        viewModel.definirOrdenacao(opcao)
                                        menuOrdenacaoAberto = false
                                    },
                                    trailingIcon = if (opcao == ordenacao) {
                                        { Icon(Icons.Default.Check, contentDescription = "Selecionada") }
                                    } else null
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (jogadores.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = abrirNovo,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Novo jogador") }
                )
            }
        }
    ) { padding ->
        if (jogadores.isEmpty()) {
            EstadoVazio(
                onCadastrar = abrirNovo,
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                item { CabecalhoLista(total = jogadores.size, ordenacao = ordenacao) }

                items(jogadores, key = { it.id }) { jogador ->
                    JogadorItem(
                        jogador = jogador,
                        onEditar = { formulario = jogador },
                        onExcluir = { jogadorParaExcluir = jogador }
                    )
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
            text = { Text("Deseja remover ${jogador.nome}? Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.remover(jogador)
                        jogadorParaExcluir = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { jogadorParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun CabecalhoLista(total: Int, ordenacao: Ordenacao) {
    val palavra = if (total == 1) "jogador" else "jogadores"
    Text(
        text = "$total $palavra · ${ordenacao.rotulo}",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
private fun EstadoVazio(onCadastrar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "A galera ainda não chegou",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Cadastre os jogadores para poder marcar presença e sortear os times.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onCadastrar) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Cadastrar primeiro jogador")
        }
    }
}

@Composable
private fun JogadorItem(
    jogador: Jogador,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarInicial(nome = jogador.nome, destacado = false)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(jogador.nome, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Estrelas(nivel = jogador.nivel)
            }
            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${jogador.nome}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onExcluir) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir ${jogador.nome}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}