package com.example.racha.ui.presenca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racha.RachaApp
import com.example.racha.data.JogadorRepository
import com.example.racha.domain.Jogador
import com.example.racha.util.JOGADORES_POR_TIME_MAXIMO
import com.example.racha.util.JOGADORES_POR_TIME_MINIMO
import com.example.racha.util.JOGADORES_POR_TIME_PADRAO
import com.example.racha.util.TIMES_MAXIMO
import com.example.racha.util.TIMES_MINIMO
import com.example.racha.util.TIMES_PADRAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class PresencaUiState(
    val jogadores: List<Jogador> = emptyList(),
    val presentesIds: Set<Long> = emptySet(),
    val quantidadeTimes: Int = TIMES_PADRAO,
    val jogadoresPorTime: Int = JOGADORES_POR_TIME_PADRAO
) {
    val totalPresentes: Int
        get() = jogadores.count { it.id in presentesIds }

    val vagas: Int
        get() = quantidadeTimes * jogadoresPorTime

    val  faltam: Int
        get() = (vagas - totalPresentes).coerceAtLeast(0)

    val reservas: Int
        get() = (totalPresentes - vagas).coerceAtLeast(0)

    val podeSortear: Boolean
        get() = totalPresentes >= vagas
}

class PresencaViewModel(
    repository: JogadorRepository
) : ViewModel() {

    private val presentesIds = MutableStateFlow<Set<Long>>(emptySet())
    private val quantidadeTimes = MutableStateFlow(TIMES_PADRAO)
    private val jogadoresPorTime = MutableStateFlow(JOGADORES_POR_TIME_PADRAO)

    val uiState: StateFlow<PresencaUiState> = combine(
        repository.jogadores,
        presentesIds,
        quantidadeTimes,
        jogadoresPorTime,
    ) { jogadores, presentes, times, porTime ->
        PresencaUiState(jogadores, presentes, times, porTime)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PresencaUiState()
    )

    fun alternarPresenca(id: Long) {
        presentesIds.update { if (id in it) it - id else it + id }
    }

    fun marcarTodos() {
        presentesIds.value = uiState.value.jogadores.map { it.id }.toSet()
    }

    fun desmarcarTodos() {
        presentesIds.value = emptySet()
    }

    fun definirQuantidadeTimes(valor: Int) {
        quantidadeTimes.value = valor.coerceIn(TIMES_MINIMO, TIMES_MAXIMO)
    }

    fun definirJogadoresPorTime(valor: Int) {
        jogadoresPorTime.value = valor.coerceIn(JOGADORES_POR_TIME_MINIMO, JOGADORES_POR_TIME_MAXIMO)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RachaApp
                PresencaViewModel(app.jogadorRepository)
            }
        }
    }
}

