package com.example.teachua_android.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsHomePageSection() {
    Column {
        TitleText(
            title = "Новини" ,
            description = "" ,
            titleFontSize = 28.sp ,
            titleLineHeight = 36.sp ,
            alignCenter = true
        )
        Spacer(modifier = Modifier.height(24.dp))
        NewsCard(
            image = R.drawable.challenge_2 ,
            date = LocalDate.of(2023 , 11 , 1) ,
            title = "Lorem ipsum dolor sit amet" ,
            onClickAction = {}
        )
        Spacer(modifier = Modifier.height(20.dp))
        NewsCard(
            image = R.drawable.challenge_2 ,
            date = LocalDate.of(2023 , 11 , 1) ,
            title = "Lorem ipsum dolor sit amet" ,
            onClickAction = {}
        )
        Spacer(modifier = Modifier.height(20.dp))
        NewsCard(
            image = R.drawable.challenge_2 ,
            date = LocalDate.of(2023 , 11 , 1) ,
            title = "Lorem ipsum dolor sit amet" ,
            onClickAction = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun NewsHomePageSectionPreview() {
    NewsHomePageSection()
}