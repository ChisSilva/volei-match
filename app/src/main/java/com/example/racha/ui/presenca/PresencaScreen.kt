package com.example.racha.ui.presenca

import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.racha.ui.componentes.AvatarInicial
import com.example.racha.ui.componentes.Estrelas
import com.example.racha.util.JOGADORES_POR_TIME_MAXIMO
import com.example.racha.util.JOGADORES_POR_TIME_MINIMO
import com.example.racha.util.TIMES_MAXIMO
import com.example.racha.util.TIMES_MINIMO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresencaScreen(
    onGerenciarJogadores: () -> Unit,
    onSortear: () -> Unit,
    onAbrirPlacar: () -> Unit,
    onAbrirAjuda: () -> Unit,
    viewModel: PresencaViewModel = viewModel(factory = PresencaViewModel.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quem vai jogar?", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAbrirAjuda) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Como usar"
                        )
                    }
                    IconButton(onClick = onAbrirPlacar) {
                        Icon(
                            painter = painterResource(R.drawable.ic_placar),
                            contentDescription = "Abrir placar",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = onGerenciarJogadores) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Gerenciar jogadores"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BarraSortear(
                habilitado = estado.podeSortear,
                onClick = { if (viewModel.sortear()) onSortear() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                ConfiguracaoCard(
                    estado = estado,
                    onTimesChange = viewModel::definirQuantidadeTimes,
                    onJogadoresPorTimeChange = viewModel::definirJogadoresPorTime
                )
            }

            item { ResumoPresenca(estado) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = viewModel::marcarTodos,
                        modifier = Modifier.weight(1f)
                    ) { Text("Marcar todos") }
                    OutlinedButton(
                        onClick = viewModel::desmarcarTodos,
                        modifier = Modifier.weight(1f)
                    ) { Text("Desmarcar todos") }
                }
            }

            if (estado.jogadores.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhum jogador cadastrado ainda.",
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onGerenciarJogadores,
                            modifier = Modifier.padding(top = 12.dp)
                        ) { Text("Cadastrar jogadores") }
                    }
                }
            } else {
                items(estado.jogadores, key = { it.id }) { jogador ->
                    PresencaItem(
                        jogador = jogador,
                        presente = jogador.id in estado.presentesIds,
                        onAlternar = { viewModel.alternarPresenca(jogador.id) }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun BarraSortear(habilitado: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Button(
                onClick = onClick,
                enabled = habilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Sortear times",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ConfiguracaoCard(
    estado: PresencaUiState,
    onTimesChange: (Int) -> Unit,
    onJogadoresPorTimeChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SeletorNumerico(
                rotulo = "Times",
                valor = estado.quantidadeTimes,
                minimo = TIMES_MINIMO,
                maximo = TIMES_MAXIMO,
                onMudar = onTimesChange
            )
            SeletorNumerico(
                rotulo = "Jogadores por time",
                valor = estado.jogadoresPorTime,
                minimo = JOGADORES_POR_TIME_MINIMO,
                maximo = JOGADORES_POR_TIME_MAXIMO,
                onMudar = onJogadoresPorTimeChange
            )
        }
    }
}

@Composable
private fun SeletorNumerico(
    rotulo: String,
    valor: Int,
    minimo: Int,
    maximo: Int,
    onMudar: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = rotulo,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        FilledTonalIconButton(
            onClick = { onMudar(valor - 1) },
            enabled = valor > minimo
        ) {
            Text("−", style = MaterialTheme.typography.titleLarge)
        }
        Text(
            text = valor.toString(),
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        FilledTonalIconButton(
            onClick = { onMudar(valor + 1) },
            enabled = valor < maximo
        ) {
            Icon(Icons.Default.Add, contentDescription = "Aumentar $rotulo")
        }
    }
}

@Composable
private fun ResumoPresenca(estado: PresencaUiState) {
    val (mensagem, cor) = when {
        !estado.podeSortear -> {
            val n = estado.faltam
            val palavra = if (n == 1) "jogador" else "jogadores"
            "Faltam $n $palavra para ${estado.quantidadeTimes} times de ${estado.jogadoresPorTime}" to
                    MaterialTheme.colorScheme.error
        }
        estado.reservas > 0 -> {
            val n = estado.reservas
            val palavra = if (n == 1) "jogador ficará" else "jogadores ficarão"
            "$n $palavra na reserva" to MaterialTheme.colorScheme.tertiary
        }
        else -> "Times completos, sem reservas" to MaterialTheme.colorScheme.primary
    }

    val progresso = if (estado.vagas == 0) 0f
    else (estado.totalPresentes.toFloat() / estado.vagas).coerceAtMost(1f)

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = estado.totalPresentes.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = cor
            )
            Text(
                text = "  presentes · ${estado.vagas} vagas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        LinearProgressIndicator(
            progress = { progresso },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = cor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = mensagem,
            color = cor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun PresencaItem(
    jogador: Jogador,
    presente: Boolean,
    onAlternar: () -> Unit
) {
    Card(
        onClick = onAlternar,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (presente) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        ),
        border = if (presente) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarInicial(nome = jogador.nome, destacado = presente)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(jogador.nome, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Estrelas(nivel = jogador.nivel)
            }
            Checkbox(checked = presente, onCheckedChange = null)
        }
    }
}