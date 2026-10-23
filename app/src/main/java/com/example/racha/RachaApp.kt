package com.example.racha

import android.app.Application
import com.example.racha.data.AppDatabase
import com.example.racha.data.JogadorRepository
import com.example.racha.domain.Sorteio
import kotlinx.coroutines.flow.MutableStateFlow

class RachaApp : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }

    val jogadorRepository by lazy { JogadorRepository(database.jogadorDao()) }

    val sorteioAtual = MutableStateFlow<Sorteio?>(null)
}