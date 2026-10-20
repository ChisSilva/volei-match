package com.example.voleimatch.data

import com.example.voleimatch.domain.Jogador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class JogadorRepository (private val dao: JogadorDao){

    val jogadores: Flow<List<Jogador>> =
        dao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    suspend fun salvar(jogador: Jogador) {
        if (jogador.id == 0L) {
            dao.inserir(jogador.toEntity())
        } else {
            dao.atualizar(jogador.toEntity())
        }
    }

    suspend fun remover(jogaodor: Jogador) {
        dao.deletar(jogaodor.toEntity())
    }
}