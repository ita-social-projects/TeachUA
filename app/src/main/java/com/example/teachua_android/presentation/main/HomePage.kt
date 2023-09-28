package com.example.teachua_android.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teachua_android.presentation.main.components.ClubsImageTitleCardPager
import com.example.teachua_android.presentation.main.components.MainHeader


@Composable
fun HomePage() {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        MainHeader(
            location = "Київ",
            onMenuButtonClick = {},
            onSearch = {},
            onAddClubsButtonClick = {},
            onLocationChange = {}
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            //TODO Add home-screen background
            /*Image(
                painter = painterResource(id = R.drawable.home_page_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )*/
            Column {
                ClubsImageTitleCardPager()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePagePreview() {
    HomePage()
}