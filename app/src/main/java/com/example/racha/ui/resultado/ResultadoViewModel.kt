package com.example.racha.ui.resultado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racha.RachaApp
import com.example.racha.domain.Sorteador
import com.example.racha.domain.Sorteio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResultadoViewModel(
    private val sorteioAtual: MutableStateFlow<Sorteio?>
) : ViewModel() {

    val sorteio: StateFlow<Sorteio?> = sorteioAtual

    fun refazer() {
        val atual = sorteioAtual.value ?: return
        sorteioAtual.value = atual.copy(
            resultado = Sorteador.sortear(
                presentes = atual.presentes,
                quantidadeTimes = atual.quantidadeTimes,
                jogadoresPorTime = atual.jogadoresPorTime
            )
        )
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RachaApp
                ResultadoViewModel(app.sorteioAtual)
            }
        }
    }
}