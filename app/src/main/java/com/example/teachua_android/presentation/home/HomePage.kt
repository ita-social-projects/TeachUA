package com.example.teachua_android.presentation.home

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import com.example.teachua_android.presentation.home.components.ClubsDirectionCard
import com.example.teachua_android.presentation.home.components.ClubsImageTitleCardPager
import com.example.teachua_android.presentation.home.components.HomeFooter
import com.example.teachua_android.presentation.home.components.MainHeader
import com.example.teachua_android.presentation.home.components.NewsCard
import com.example.teachua_android.presentation.home.components.OrangeButton
import com.example.teachua_android.presentation.home.components.TitleText
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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
                            "Тут ви можете знайти мотиваційні та " +
                            "практичні вебінари з експертами, " +
                            "корисні матеріали, які вдосконалять ваші знання та" +
                            " навички викладати " +
                            "українською.",
                    titleFontSize = 28.sp,
                    titleLineHeight = 28.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                OrangeButton(
                    text = "Переглянути матеріали",
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Всі гуртки",

                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFA8C16),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                TitleText(
                    title = "Новини",
                    subtext = "",
                    titleFontSize = 28.sp,
                    titleLineHeight = 36.sp,
                    alignCenter = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                NewsCard(
                    image = R.drawable.challenge_2,
                    date = LocalDate.of(2023, 11, 1),
                    title = "Lorem ipsum dolor sit amet",
                    onClickAction = {}
                )
                Spacer(modifier = Modifier.height(20.dp))
                NewsCard(
                    image = R.drawable.challenge_2,
                    date = LocalDate.of(2023, 11, 1),
                    title = "Lorem ipsum dolor sit amet",
                    onClickAction = {}
                )
                Spacer(modifier = Modifier.height(20.dp))
                NewsCard(
                    image = R.drawable.challenge_2,
                    date = LocalDate.of(2023, 11, 1),
                    title = "Lorem ipsum dolor sit amet",
                    onClickAction = {}
                )
                Spacer(modifier = Modifier.height(32.dp))
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
                Spacer(modifier = Modifier.height(20.dp))
                TitleText(
                    title = "Челендж “Навчай українською”",
                    subtext = "Близько тисячі учасників з усієї України " +
                            "уже взяли участь у 21-денному челенджі " +
                            "“Навчай українською” для закладів позашкільної " +
                            "освіти, які переходять на українську мову навчання." +
                            " У листопаді 2020 року на українську мову викладання" +
                            " перейшло близько пів сотні гуртків. Ми підготували" +
                            " матеріали для тих, хто хоче перейти на українську.",
                    titleFontSize = 28.sp,
                    titleLineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                OrangeButton(
                    text = "Переглянути матеріали",
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(42.dp))

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
    HomePage()
}