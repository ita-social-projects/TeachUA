package com.example.teachua_android.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import com.example.teachua_android.presentation.ui.theme.GreyPrimary
import com.example.teachua_android.presentation.ui.theme.OrangePrimary
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun ClubsImageTitleCard(
    @DrawableRes image: Int,
    title: String,
    subText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        size = 8.dp
                    )
                )
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 36.sp,
                fontWeight = FontWeight(700),
                lineHeight = 54.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = subText,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight(700),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OrangeButtonWithWhiteText(text = stringResource(id = R.string.details), onClick = onClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ClubsImageTitleCardPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val pageCount = 4
    Column(
        modifier = modifier.wrapContentSize()
    ) {
        HorizontalPager(
            count = pageCount,
            state = pagerState,
            modifier = Modifier,
            itemSpacing = 24.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            ClubsImageTitleCard(
                image = R.drawable.homepage_moreinfo_img1,
                title = "Про гуртки українською",
                subText = "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.\n",
                onClick = {},
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) OrangePrimary else GreyPrimary
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF090101)
@Composable
fun ClubsImageTitleCardPagerPreview() {
    ClubsImageTitleCardPager()
}


@Preview()
@Composable
fun ClubsImageTitleCardPreview() {
    ClubsImageTitleCard(
        image = R.drawable.homepage_moreinfo_img1,
        title = "Про гуртки українською",
        subText = "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.\n",
        onClick = {}
    )
}
