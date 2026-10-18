package com.example.voleimatch.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.voleimatch.domain.Jogador

@Entity(tableName = "jogadores")
data class JogadorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val nivel: Int
)

fun JogadorEntity.toDomain() = Jogador(id = id, nome = nome, nivel = nivel)

fun Jogador.toEntity() = JogadorEntity(id = id, nome = nome, nivel = nivel)