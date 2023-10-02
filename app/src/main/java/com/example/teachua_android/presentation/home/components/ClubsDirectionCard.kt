package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClubsDirectionCard(
    icon: ImageVector,
    title: String,
    subText: String,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(Color.White),
        modifier = modifier.size(width = 240.dp, height = 348.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF2D4C68),
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subText,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF2D4C68),
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
            Spacer(modifier = Modifier.weight(1f))
            CheckTextLink(onCheckClick = onCheckClick)
        }
    }
}

@Composable
fun CheckTextLink(
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.clickable(onClick = onCheckClick)
    ) {
        Text(
            text = "Переглянути",

            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF2E69C9),
                textAlign = TextAlign.Center,
            )
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "image description",
            tint = Color(0xFF2E69C9),
            modifier = Modifier
                .padding(1.dp)
                .width(20.dp)
                .height(20.dp)
        )
    }
}

@Preview
@Composable
fun ClubsDirectionCardPreview() {
    ClubsDirectionCard(
        title = "Вокальна студія, музика, музичні інструменти",
        icon = Icons.Default.ArrowForward,
        subText = "Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін.",
        onCheckClick = {}
    )
}