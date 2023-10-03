package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun ClubsDirectionHomePageSection() {
    Column {
        TitleText(
            title = "Оберіть напрям гуртків",
            subtext = "",
            titleFontSize = 28.sp,
            titleLineHeight = 36.sp
        )
        ClubsDirectionCard(
            title = "Вокальна студія, музика, музичні інструменти",
            icon = Icons.Default.ArrowForward,
            subText = "Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін.",
            onCheckClick = {}
        )
    }
}

@Preview
@Composable
fun ClubsDirectionHomePageSectionPreview() {
    ClubsDirectionHomePageSection()
}