package com.example.voleimatch

import android.app.Application
import com.example.voleimatch.data.AppDatabase
import com.example.voleimatch.data.JogadorRepository

class VoleiApp : Application {

    private val database by lazy { AppDatabase.getInstance(this) }

    val jogadorRepository by lazy { JogadorRepository(database.jogadorDao()) }
}