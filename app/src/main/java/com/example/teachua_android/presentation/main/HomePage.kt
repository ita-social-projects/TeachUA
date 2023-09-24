package com.example.teachua_android.presentation.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
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

            HomePageClubsImageTitleCard(
                image = R.drawable.homepage_moreinfo_img1,
                title = "Про гуртки українською",
                subText = "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.\n",
                onClick = {},
                modifier = Modifier.padding(top = 16.dp)
            )

        }
    }
}

@Composable
fun HomePageClubsImageTitleCard(
    @DrawableRes image: Int,
    title: String,
    subText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(343.dp)
            .height(460.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 36.sp,
                fontWeight = FontWeight(700),
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

            Button(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.details),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun HomePageClubsImageTitleCardPreview() {
    HomePageClubsImageTitleCard(
        image = R.drawable.homepage_moreinfo_img1,
        title = "Про гуртки українською",
        subText = "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.\n",
        onClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun HomePagePreview() {
    HomePage()
}