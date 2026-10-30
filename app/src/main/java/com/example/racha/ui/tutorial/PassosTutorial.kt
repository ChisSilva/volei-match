package com.example.racha.ui.tutorial

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.example.racha.R

data class PassoTutorial(
    val titulo: String,
    val texto: String,
    val icone: @Composable () -> Painter
)

val PassosDoTutorial = listOf(
    PassoTutorial(
        titulo = "Cadastre a galera",
        texto = "Adicione os jogadores e dê a cada um de 1 a 5 estrelas, conforme o nível de jogo. " +
                "É esse nível que o app usa para deixar os times equilibrados.",
        icone = { rememberVectorPainter(Icons.Default.Person) }
    ),
    PassoTutorial(
        titulo = "Marque quem veio",
        texto = "Na tela inicial, marque os jogadores que estão presentes no dia e escolha " +
                "quantos times e quantos jogadores por time. Se sobrar gente, vira reserva.",
        icone = { rememberVectorPainter(Icons.Default.CheckCircle) }
    ),
    PassoTutorial(
        titulo = "Sorteie e compartilhe",
        texto = "Toque em Sortear times. Não gostou do resultado? Refaça o sorteio quantas vezes " +
                "quiser. Depois, copie a lista e cole direto no WhatsApp.",
        icone = { rememberVectorPainter(Icons.Default.Share) }
    ),
    PassoTutorial(
        titulo = "Acompanhe no placar",
        texto = "Abra o placar pelo ícone da tela inicial ou pelo botão Iniciar placar, na tela do " +
                "resultado. Ele abre deitado, com números grandes, e marca pontos e faltas.",
        icone = { painterResource(R.drawable.ic_placar) }
    )
)