package com.example.voleimatch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JogadorDao {

    @Query("SELECT * FROM jogadores ORDER BY nome COLLATE NOCASE ASC")
    fun observarTodos(): Flow<List<JogadorEntity>>

    @Insert
    suspend fun inserir(jogador: JogadorEntity)

    @Update
    suspend fun atualizar(jogador: JogadorEntity)

    @Delete
    suspend fun deletar(jogador: JogadorEntity)
}