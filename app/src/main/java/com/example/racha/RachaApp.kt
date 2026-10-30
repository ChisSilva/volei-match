package com.example.racha

import com.example.racha.data.PreferenciasApp
import android.app.Application
import com.example.racha.data.AppDatabase
import com.example.racha.data.JogadorRepository
import com.example.racha.domain.Sorteio
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.racha.domain.Placar

class RachaApp : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }

    val jogadorRepository by lazy {
        JogadorRepository(database.jogadorDao())
    }
    val preferencias by lazy {
        PreferenciasApp(this)
    }

    val sorteioAtual = MutableStateFlow<Sorteio?>(null)

    val placarAtual = MutableStateFlow(Placar())
}