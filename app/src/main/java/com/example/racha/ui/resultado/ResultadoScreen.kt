package com.example.racha.ui.resultado

import androidx.compose.material3.FilledTonalButton
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha.domain.Jogador
import com.example.racha.domain.Time
import com.example.racha.util.FormatadorWhatsApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoScreen(
    onVoltar: () -> Unit,
    onAbrirPlacar: () -> Unit,
    viewModel: ResultadoViewModel = viewModel(factory = ResultadoViewModel.Factory)
) {
    val sorteio by viewModel.sorteio.collectAsStateWithLifecycle()
    val resultado = sorteio?.resultado

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Times sorteados") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (resultado != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onAbrirPlacar,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Iniciar placar") }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::refazer,
                            modifier = Modifier.weight(1f)
                        ) { Text("Refazer sorteio") }

                        Button(
                            onClick = {
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val texto = FormatadorWhatsApp.formatar(resultado)
                                clipboard.setPrimaryClip(ClipData.newPlainText("Times", texto))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Copiado! Agora é só colar no WhatsApp.")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("Copiar para WhatsApp") }
                    }
                }
            }
        }
    ) { padding ->
        if (resultado == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum sorteio feito ainda.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(resultado.times, key = { it.numero }) { time ->
                    TimeCard(time)
                }

                if (resultado.reservas.isNotEmpty()) {
                    item { ReservasCard(resultado.reservas) }
                }
            }
        }
    }
}

@Composable
private fun TimeCard(time: Time) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Time ${time.numero}", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Soma ${time.somaNiveis} • Média ${FormatadorWhatsApp.formatarMedia(time.mediaNiveis)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            time.jogadores.forEach { JogadorLinha(it) }
        }
    }
}

@Composable
private fun ReservasCard(reservas: List<Jogador>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Reservas (${reservas.size})", style = MaterialTheme.typography.titleLarge)
            reservas.forEach { JogadorLinha(it) }
        }
    }
}

@Composable
private fun JogadorLinha(jogador: Jogador) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(jogador.nome, modifier = Modifier.weight(1f))
        Text("⭐".repeat(jogador.nivel), style = MaterialTheme.typography.bodySmall)
    }
}