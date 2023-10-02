package com.example.teachua_android.presentation.home.components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import java.time.LocalDate

@Composable
fun NewsCard(
    @DrawableRes image: Int,
    date: LocalDate,
    title: String,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Column {
                Text(
                    text = date.toString(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF2D4C68),
                    )
                )
                TitleText(
                    title = title, subtext = "",
                    titleFontSize = 18.sp, titleLineHeight = 28.sp
                )
            }
            CheckTextLink(onCheckClick = onClickAction)
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun NewsCardPreview() {
    NewsCard(
        image = R.drawable.challenge_2,
        date = LocalDate.of(2023, 11, 1),
        title = "Lorem ipsum dolor sit amet",
        onClickAction = {}
    )
}