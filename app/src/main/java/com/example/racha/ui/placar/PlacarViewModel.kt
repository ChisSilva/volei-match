package com.example.racha.ui.placar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racha.RachaApp
import com.example.racha.domain.Lado
import com.example.racha.domain.Placar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PlacarViewModel(
    private val placarAtual: MutableStateFlow<Placar>
) : ViewModel() {

    val placar: StateFlow<Placar> = placarAtual

    fun alterarPontos(lado: Lado, delta: Int) =
        placarAtual.update { it.alterarPontos(lado, delta) }

    fun alterarFaltas(lado: Lado, delta: Int) =
        placarAtual.update { it.alterarFaltas(lado, delta) }

    fun trocarLados() = placarAtual.update { it.trocarLados() }

    fun zerar() {
        placarAtual.value = Placar()
    }

    fun definir(novo: Placar) {
        placarAtual.value = novo.limitado()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RachaApp
                PlacarViewModel(app.placarAtual)
            }
        }
    }
}