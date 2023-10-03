package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WhiteButtonWithOrangeText(text: String , onClick: () -> Unit , modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(6.dp) ,
        modifier = modifier
    ) {
        Text(
            text = text,

            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(700) ,
                color = Color(0xFFFA8C16) ,
                textAlign = TextAlign.Center,
            )
        )
    }
}