package com.example.voleimatch.ui.jogadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.voleimatch.VoleiApp
import com.example.voleimatch.data.JogadorRepository
import com.example.voleimatch.domain.Jogador
import com.example.voleimatch.util.NIVEL_MAXIMO
import com.example.voleimatch.util.NIVEL_MINIMO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JogadoresViewModel(
    private val repository: JogadorRepository
) : ViewModel() {

    val jogadores: StateFlow<List<Jogador>> = repository.jogadores
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun salvar(id: Long, nome: String, nivel: Int) {
        // Validação dos dados
        val nomeLimpo = nome.trim()
        if (nomeLimpo.isEmpty() || nivel !in NIVEL_MINIMO..NIVEL_MAXIMO) return

        viewModelScope.launch {
            repository.salvar(Jogador(id = id, nome = nomeLimpo, nivel = nivel))
        }
    }

    fun remover(jogador: Jogador) {
        viewModelScope.launch {
            repository.remover(jogador)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as VoleiApp
                JogadoresViewModel(app.jogadorRepository)
            }
        }
    }
}