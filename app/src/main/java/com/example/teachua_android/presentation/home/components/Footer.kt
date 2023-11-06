package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R

@Composable
fun HomeFooter(
    onHelpButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        TitleText(
            title = "Наші партнери",
            description = "",
            titleFontSize = 20.sp,
            titleLineHeight = 28.sp,
            alignCenter = true
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SponsorLogo(painterResource(id = R.drawable.logo1))
            SponsorLogo(painterResource(id = R.drawable.logo2))
            SponsorLogo(painterResource(id = R.drawable.logo3))
            SponsorLogo(painterResource(id = R.drawable.logo4))
            SponsorLogo(painterResource(id = R.drawable.soft_serve_logo))
        }
        Spacer(modifier = Modifier.height(30.dp))
        TitleText(
            title = "Як допомогти проєкту",
            description = "Ініціатива потребує постійної фінансової\n " +
                    "підтримки, аби покривати щоденні витрати.",
            titleFontSize = 20.sp,
            titleLineHeight = 28.sp,
            alignCenter = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        OrangeButtonWithWhiteText(
            text = "Допомогти проєкту",
            onClick = onHelpButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painterResource(id = R.drawable.logofooter), contentDescription = "Speak-Ukrainian",
            modifier = Modifier.height(40.dp).width(137.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Нам небайдуже майбутнє дітей та української мови.",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF2D4C68),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painterResource(id = R.drawable.social), contentDescription = "socials",
            modifier = Modifier.height(22.dp).width(160.dp),
            //contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "©2021 Design by Qubstudio & Development by SoftServe",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF2D4C68),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(26.dp))

    }
}

@Composable
fun SponsorLogo(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = "Sponsor Logo",
        modifier = Modifier
            .width(62.dp)
            .height(62.dp)
            .clip(RoundedCornerShape(4.dp))
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true, showSystemUi = true)
@Composable
fun HomeFooterPreview() {
    Surface {
        HomeFooter(onHelpButtonClick = {})
    }
}