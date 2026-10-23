package com.example.racha.ui.presenca

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha.domain.Jogador
import com.example.racha.util.JOGADORES_POR_TIME_MAXIMO
import com.example.racha.util.JOGADORES_POR_TIME_MINIMO
import com.example.racha.util.TIMES_MAXIMO
import com.example.racha.util.TIMES_MINIMO
import androidx.compose.foundation.layout.height

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresencaScreen(
    onGerenciarJogadores: () -> Unit,
    viewModel: PresencaViewModel = viewModel(factory = PresencaViewModel.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quem vai jogar?") },
                actions = {
                    IconButton(onClick = onGerenciarJogadores) {
                        Icon(Icons.Default.Person, contentDescription = "Gerenciar jogadores")
                    }
                }
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
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = viewModel::marcarTodos) { Text("Marcar todos") }
                    OutlinedButton(onClick = viewModel::desmarcarTodos) { Text("Desmarcar todos") }
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
                item { Spacer(Modifier.height(8.dp)) }

                items(estado.jogadores, key = { it.id }) { jogador ->
                    PresencaItem(
                        jogador = jogador,
                        presente = jogador.id in estado.presentesIds,
                        onAlternar = { viewModel.alternarPresenca(jogador.id) }
                    )
                    HorizontalDivider()
                }
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
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
        Text(text = rotulo, modifier = Modifier.weight(1f))
        OutlinedIconButton(onClick = { onMudar(valor - 1) }, enabled = valor > minimo) {
            Text("−")
        }
        Text(
            text = valor.toString(),
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedIconButton(onClick = { onMudar(valor + 1) }, enabled = valor < maximo) {
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
            "$n $palavra na reserva" to MaterialTheme.colorScheme.primary
        }
        else -> "Times completos, sem reservas" to MaterialTheme.colorScheme.primary
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "${estado.totalPresentes} presentes (${estado.vagas} vagas)",
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = mensagem, color = cor, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun PresencaItem(
    jogador: Jogador,
    presente: Boolean,
    onAlternar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAlternar)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = presente, onCheckedChange = null)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(jogador.nome)
            Text("⭐".repeat(jogador.nivel), style = MaterialTheme.typography.bodySmall)
        }
    }
}