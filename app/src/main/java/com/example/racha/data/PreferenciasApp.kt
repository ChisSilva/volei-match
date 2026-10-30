package com.example.racha.data

import android.content.Context
import androidx.core.content.edit

class PreferenciasApp(context: Context) {

    private val prefs = context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE)

    var tutorialVisto: Boolean
        get() = prefs.getBoolean(CHAVE_TUTORIAL_VISTO, false)
        set(valor) = prefs.edit { putBoolean(CHAVE_TUTORIAL_VISTO, valor) }

    private companion object {
        const val NOME_ARQUIVO = "racha_preferencias"
        const val CHAVE_TUTORIAL_VISTO = "tutorial_visto"
    }
}