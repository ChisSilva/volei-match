package com.example.racha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racha.RachaApp
import com.example.racha.ui.jogadores.JogadoresScreen
import com.example.racha.ui.placar.PlacarScreen
import com.example.racha.ui.presenca.PresencaScreen
import com.example.racha.ui.resultado.ResultadoScreen
import com.example.racha.ui.tutorial.TutorialScreen

object Rotas {
    const val PRESENCA = "presenca"
    const val JOGADORES = "jogadores"
    const val RESULTADO = "resultado"
    const val PLACAR = "placar"
    const val TUTORIAL = "tutorial"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as RachaApp

    // Primeira abertura: começa no tutorial. Nas seguintes: direto na presença.
    val destinoInicial = remember {
        if (app.preferencias.tutorialVisto) Rotas.PRESENCA else Rotas.TUTORIAL
    }

    val concluirTutorial: () -> Unit = {
        app.preferencias.tutorialVisto = true
        if (navController.previousBackStackEntry != null) {
            // Aberto pelo ícone de ajuda: volta para a tela de onde veio
            navController.popBackStack()
        } else {
            // Primeira abertura: vai para a presença e tira o tutorial da pilha
            navController.navigate(Rotas.PRESENCA) {
                popUpTo(Rotas.TUTORIAL) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable(Rotas.PRESENCA) {
            PresencaScreen(
                onGerenciarJogadores = { navController.navigate(Rotas.JOGADORES) },
                onSortear = { navController.navigate(Rotas.RESULTADO) },
                onAbrirPlacar = { navController.navigate(Rotas.PLACAR) },
                onAbrirAjuda = { navController.navigate(Rotas.TUTORIAL) }
            )
        }
        composable(Rotas.JOGADORES) {
            JogadoresScreen(onVoltar = { navController.popBackStack() })
        }
        composable(Rotas.RESULTADO) {
            ResultadoScreen(
                onVoltar = { navController.popBackStack() },
                onAbrirPlacar = { navController.navigate(Rotas.PLACAR) }
            )
        }
        composable(Rotas.PLACAR) {
            PlacarScreen(onVoltar = { navController.popBackStack() })
        }
        composable(Rotas.TUTORIAL) {
            TutorialScreen(onConcluir = concluirTutorial)
        }
    }
}