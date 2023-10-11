package com.example.teachua_android.presentation.navigation_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NotImplementedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Цю секцію ще не розроблено")
    }
}

@Preview
@Composable
fun NotImplementedScreenPreview() {
    Surface {
        NotImplementedScreen()
    }
}