package com.example.teachua_android.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import com.example.teachua_android.presentation.home.components.ClubsImageTitleCardPager
import com.example.teachua_android.presentation.home.components.MainHeader
import com.example.teachua_android.presentation.home.components.TitleText
import com.example.teachua_android.presentation.ui.theme.OrangePrimary

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
            onLocationChange = {},
        )
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
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                ClubsImageTitleCardPager(modifier = Modifier.padding(top = 16.dp))
                TitleText(
                    title = "Про нас",
                    subtext = "Ініціатива \"Навчай українською\" - " +
                            "це небайдужі громадяни, які об'єдналися, " +
                            "щоб популяризувати українську мову у сфері освіти.",
                    titleLineHeight = 32.sp,
                    titleFontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.challenge_2),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.225f)
                        .clip(RoundedCornerShape(8.dp))
                        .align(Alignment.CenterHorizontally)
                )
                TitleText(
                    title = "Про челендж \"Навчай українською\"",
                    subtext = "Ми допоможемо вам перейти на українську мову викладання. " +
                            "Тут ви можете знайти мотиваційні та практичні вебінари з експертами, " +
                            "корисні матеріали, які вдосконалять ваші знання та" +
                            " навички викладати " +
                            "українською.",
                    titleFontSize = 28.sp,
                    titleLineHeight = 28.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                Button(
                    onClick = { /*TODO add button functionality*/ },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(OrangePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Переглянути матеріали")
                }
                Spacer(modifier = Modifier.height(32.dp))
                TitleText(
                    title = "Оберіть напрям гуртків",
                    subtext = "",
                    titleFontSize = 28.sp,
                    titleLineHeight = 36.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, device = "spec:width=407dp,height=2340dp")
fun HomePagePreview() {
    HomePage()
}