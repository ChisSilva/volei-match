package com.example.racha.domain

import java.text.Collator
import java.util.Locale

enum class Ordenacao(val rotulo: String) {
    NOME_CRESCENTE("Nome (A → Z)"),
    NOME_DECRESCENTE("Nome (Z → A)"),
    NIVEL_DECRESCENTE("Nível (maior primeiro)"),
    NIVEL_CRESCENTE("Nível (menor primeiro)");

    fun ordenar(jogadores: List<Jogador>): List<Jogador> {
        val collator = Collator.getInstance(Locale.forLanguageTag("pt-BR"))
        collator.strength = Collator.PRIMARY // ignora maiúsculas e acentos

        val porNome = compareBy<Jogador, String>(collator) { it.nome }

        return when (this) {
            NOME_CRESCENTE -> jogadores.sortedWith(porNome)
            NOME_DECRESCENTE -> jogadores.sortedWith(porNome.reversed())
            NIVEL_DECRESCENTE ->
                jogadores.sortedWith(compareByDescending<Jogador> { it.nivel }.then(porNome))
            NIVEL_CRESCENTE ->
                jogadores.sortedWith(compareBy<Jogador> { it.nivel }.then(porNome))
        }
    }
}