package com.example.teachua_android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teachua_android.presentation.main.MainHeader
import com.example.teachua_android.presentation.ui.theme.TeachUA_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeachUA_androidTheme {
               MainHeader()
            }
        }
    }
}
