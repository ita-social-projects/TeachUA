package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun TeachUkrainianChallengeHomePageSection() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.challenge_2) ,
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
        OrangeButtonWithWhiteText(
            text = "Переглянути матеріали",
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun TeachUkrainianChallengeHomePageSectionPreview() {
    TeachUkrainianChallengeHomePageSection()
}