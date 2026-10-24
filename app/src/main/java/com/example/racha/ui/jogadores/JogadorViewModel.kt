package com.example.racha.ui.jogadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racha.RachaApp
import com.example.racha.data.JogadorRepository
import com.example.racha.domain.Jogador
import com.example.racha.domain.Ordenacao
import com.example.racha.util.NIVEL_MAXIMO
import com.example.racha.util.NIVEL_MINIMO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JogadoresViewModel(
    private val repository: JogadorRepository
) : ViewModel() {

    private val _ordenacao = MutableStateFlow(Ordenacao.NOME_CRESCENTE)
    val ordenacao: StateFlow<Ordenacao> = _ordenacao

    val jogadores: StateFlow<List<Jogador>> = combine(
        repository.jogadores,
        _ordenacao
    ) { lista, ordem -> ordem.ordenar(lista) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun definirOrdenacao(nova: Ordenacao) {
        _ordenacao.value = nova
    }

    fun salvar(id: Long, nome: String, nivel: Int) {
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
                val app = this[APPLICATION_KEY] as RachaApp
                JogadoresViewModel(app.jogadorRepository)
            }
        }
    }
}