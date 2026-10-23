package com.example.racha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racha.ui.jogadores.JogadoresScreen
import com.example.racha.ui.presenca.PresencaScreen
import com.example.racha.ui.resultado.ResultadoScreen

object Rotas {
    const val PRESENCA = "presenca"
    const val JOGADORES = "jogadores"
    const val RESULTADO = "resultado"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rotas.PRESENCA) {
        composable(Rotas.PRESENCA) {
            PresencaScreen(
                onGerenciarJogadores = { navController.navigate(Rotas.JOGADORES) },
                onSortear = { navController.navigate(Rotas.RESULTADO) }
            )
        }
        composable(Rotas.JOGADORES) {
            JogadoresScreen()
        }
        composable(Rotas.RESULTADO) {
            ResultadoScreen(onVoltar = { navController.popBackStack() })
        }
    }
}