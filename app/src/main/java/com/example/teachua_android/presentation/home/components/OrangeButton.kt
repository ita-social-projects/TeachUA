package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.teachua_android.presentation.ui.theme.OrangePrimary

@Composable
fun OrangeButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = OrangePrimary),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontFamily.SansSerif
        )
    }
}