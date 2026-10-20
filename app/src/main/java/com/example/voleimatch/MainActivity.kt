package com.example.voleimatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.voleimatch.ui.jogadores.JogadoresScreen
import com.example.voleimatch.ui.theme.VoleiMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoleiMatchTheme {
                JogadoresScreen()
            }
        }
    }
}