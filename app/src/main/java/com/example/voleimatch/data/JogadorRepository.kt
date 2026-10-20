package com.example.voleimatch.data

import com.example.voleimatch.domain.Jogador

class JogadorRepository (private val dao: JogadorDao){

    val jogadores: Flow<list<Jogador>> =
        dao.observarTodos().map { lista.map { it.toDomain() } }

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