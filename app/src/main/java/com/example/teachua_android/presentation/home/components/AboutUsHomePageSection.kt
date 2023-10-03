package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun AboutUsHomePageSection() {
    Column {
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
            painter = painterResource(id = R.drawable.challenge_2) ,
            contentDescription = "image description",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.225f)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun AboutUsHomePageSectionPreview() {
    AboutUsHomePageSection()
}