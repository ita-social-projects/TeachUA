package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClubsDirectionHomePageSection(modifier: Modifier = Modifier) {
    Column {
        TitleText(
            title = "Оберіть напрям гуртків" ,
            subtext = "" ,
            titleFontSize = 28.sp ,
            titleLineHeight = 36.sp,
            Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(modifier = modifier) {
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
            items(5) {
                ClubsDirectionCard(
                    title = "Вокальна студія, музика, музичні інструменти" ,
                    icon = Icons.Default.ArrowForward ,
                    subText = "Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін." ,
                    onCheckClick = {}
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

        }
    }
}

@Preview
@Composable
fun ClubsDirectionHomePageSectionPreview() {
    ClubsDirectionHomePageSection()
}