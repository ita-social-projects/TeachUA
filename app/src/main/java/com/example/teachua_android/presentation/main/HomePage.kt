package com.example.teachua_android.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teachua_android.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


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
                HomePageClubsImageTitleCardPager()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePageClubsImageTitleCardPager() {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = 4,
        state = pagerState,
    ) {
        HomePageClubsImageTitleCard(
            image = R.drawable.homepage_moreinfo_img1,
            title = "Про гуртки українською",
            subText = "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.\n",
            onClick = {},
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )
    }
}


@Composable
@Preview(showBackground = true)
fun HomePagePreview() {
    HomePage()
}