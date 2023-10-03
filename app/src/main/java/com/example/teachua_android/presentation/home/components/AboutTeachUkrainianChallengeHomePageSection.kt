package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutTeachUkrainianChallengeHomePageSection() {
    Column {
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
        OrangeButtonWithWhiteText(
            text = "Переглянути матеріали",
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AboutTeachUkrainianChallengeHomePageSectionPreview() {
    AboutTeachUkrainianChallengeHomePageSection()
}