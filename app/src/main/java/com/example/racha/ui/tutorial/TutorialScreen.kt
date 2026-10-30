package com.example.racha.ui.tutorial

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(onConcluir: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { PassosDoTutorial.size })
    val scope = rememberCoroutineScope()
    val pagina = pagerState.currentPage
    val ultima = pagina == PassosDoTutorial.lastIndex

    // Botão voltar do sistema = Pular
    BackHandler(onBack = onConcluir)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onConcluir) { Text("Pular") }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { indice ->
                PassoPagina(PassosDoTutorial[indice])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(112.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (pagina > 0) {
                        TextButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(pagina - 1) } }
                        ) { Text("Voltar") }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Indicador(total = PassosDoTutorial.size, atual = pagina)
                }

                Box(
                    modifier = Modifier.width(112.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (ultima) {
                        Button(onClick = onConcluir) { Text("Começar") }
                    } else {
                        Button(
                            onClick = { scope.launch { pagerState.animateScrollToPage(pagina + 1) } }
                        ) { Text("Próximo") }
                    }
                }
            }
        }
    }
}

@Composable
private fun PassoPagina(passo: PassoTutorial) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = passo.icone(),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.height(28.dp))
            Text(
                text = passo.titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = passo.texto,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Indicador(total: Int, atual: Int) {
    Row(
        modifier = Modifier.semantics { contentDescription = "Passo ${atual + 1} de $total" },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { indice ->
            val selecionado = indice == atual
            val largura by animateDpAsState(
                targetValue = if (selecionado) 24.dp else 8.dp,
                label = "larguraDoPonto"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(largura)
                    .clip(CircleShape)
                    .background(
                        if (selecionado) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
            )
        }
    }
}