package com.example.voleimatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voleimatch.ui.jogadores.JogadoresScreen
import com.example.voleimatch.ui.presenca.PresencaScreen

object Rotas {
    const val PRESENCA = "presenca"
    const val JOGADORES = "jogadores"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rotas.PRESENCA) {
        composable(Rotas.PRESENCA) {
            PresencaScreen(onGerenciarJogadores = { navController.navigate(Rotas.JOGADORES) })
        }
        composable(Rotas.JOGADORES) {
            JogadoresScreen()
        }
    }
}