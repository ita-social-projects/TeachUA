package com.example.teachua_android.presentation.home

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teachua_android.R
import com.example.teachua_android.presentation.home.components.AboutTeachUkrainianChallengeHomePageSection
import com.example.teachua_android.presentation.home.components.AboutUsHomePageSection
import com.example.teachua_android.presentation.home.components.ClubsDirectionHomePageSection
import com.example.teachua_android.presentation.home.components.ClubsImageTitleCardPager
import com.example.teachua_android.presentation.home.components.HomeFooter
import com.example.teachua_android.presentation.home.components.NewsHomePageSection
import com.example.teachua_android.presentation.home.components.TeachUkrainianChallengeHomePageSection
import com.example.teachua_android.presentation.home.components.WhiteButtonWithOrangeText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePageContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_page_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Column {
                ClubsImageTitleCardPager(modifier = Modifier.padding(top = 16.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    AboutUsHomePageSection()
                    AboutTeachUkrainianChallengeHomePageSection()
                    Spacer(modifier = Modifier.height(32.dp))
                }
                ClubsDirectionHomePageSection(modifier = Modifier)
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    WhiteButtonWithOrangeText(text = "Всі гуртки" , onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(32.dp))
                    NewsHomePageSection()
                    Spacer(modifier = Modifier.height(32.dp))
                    TeachUkrainianChallengeHomePageSection()
                    Spacer(modifier = Modifier.height(42.dp))
                }

            }
        }
        HomeFooter({})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(
    showBackground = true, device = "spec:width=407dp,height=5000dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun HomePagePreview() {
    HomePageContent()
}