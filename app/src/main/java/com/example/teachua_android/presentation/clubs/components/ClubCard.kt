package com.example.teachua_android.presentation.clubs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R
import com.example.teachua_android.domain.model.club.Category
import com.example.teachua_android.domain.model.club.Club
import com.example.teachua_android.domain.model.club.Location
import com.example.teachua_android.presentation.home.components.TitleText
import com.example.teachua_android.presentation.home.components.WhiteButtonWithOrangeText
import com.example.teachua_android.presentation.ui.theme.LightBlue
import com.example.teachua_android.presentation.ui.theme.OrangePrimary
import com.example.teachua_android.presentation.ui.theme.SubtextGray

@Composable
fun ClubCard(club: Club , modifier: Modifier = Modifier) {

    Card(
        shape = RoundedCornerShape(16.dp) ,
        backgroundColor = Color.White ,
        elevation = 10.dp ,
        modifier = modifier
    ) {
        Column(
            Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically ,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LightBlue) ,
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_club_default) ,
                        contentDescription = null ,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                TitleText(
                    title = club.name ,
                    subtext = "" ,
                    titleFontSize = 16.sp ,
                    titleLineHeight = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (club.categories.isNotEmpty()) {
                    CategoryTag(category = club.categories.first())
                }
                if (club.categories.size > 1) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryTag(category = club.categories[1])
                        Spacer(modifier = Modifier.width(16.dp))
                        if (club.categories.size > 2) {
                            Text(text = "і ще ${club.categories.size - 2}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(if (club.categories.size == 1) 52.dp else 32.dp))
            Text(
                text = club.description,
                style = TextStyle(
                    fontSize = 14.sp ,
                    lineHeight = 24.sp ,
                    fontWeight = FontWeight(400) ,
                    color = SubtextGray ,
                ) ,
                maxLines = 5 ,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                for (i in 0..4) {
                    Icon(Icons.Default.Star , contentDescription = null , tint = Color(0xFF777777))
                }
            }
            if (club.locations.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn ,
                        contentDescription = null ,
                        tint = OrangePrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        club.locations.first().street ,
                        style = TextStyle(
                            fontSize = 14.sp ,
                            lineHeight = 24.sp ,
                            fontWeight = FontWeight(600) ,
                            color = Color(0xFF2D4C68)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            WhiteButtonWithOrangeText(
                text = stringResource(id = R.string.details) ,
                onClick = { /*TODO*/ } ,
                Modifier
                    .fillMaxWidth()
                    .border(1.dp , OrangePrimary , RoundedCornerShape(6.dp))
            )
        }
    }
}


@Preview
@Composable
fun ClubCardPreview() {
    /*Surface {
    }*/
    ClubCard(
        Club(
            1 ,
            "Довкілля крізь призму\nукраїнської мови" ,
            description = "Американський гімнастичний клуб (American Gymnastics Club) – перша та єдина в країні мережа унікальних спортивних клубів, яка базується на Розвивальній Гімнастиці. Крім щоденних занять, в Американському гімнастичному Клубі проходять «Показові виступи» та різноманітні тематичні вечірки, які допомагають зібрати активних однодумців і популяризувати та прививати любов до спорту, перетворюючи його в стиль життя." ,
            "urlLogo" ,
            null ,
            categories = listOf(
                Category(
                    id = 4 ,
                    sortBy = 0 ,
                    name = "Програмування, робототехніка, STEM" ,
                    description = "Вивчення природничих наук, технологій , інженерії " +
                            "та математики , STEM - освіта" ,
                    urlLogo = "/static/images/categories/programming.svg" ,
                    backgroundColor = "#597EF7" ,
                    tagBackgroundColor = "#597EF7" ,
                    tagTextColor = "#ffffff"
                ) , Category(
                    id = 4 ,
                    sortBy = 0 ,
                    name = "Програмування, робототехніка, STEM" ,
                    description = "Вивчення природничих наук, технологій , інженерії " +
                            "та математики , STEM - освіта" ,
                    urlLogo = "/static/images/categories/programming.svg" ,
                    backgroundColor = "#597EF7" ,
                    tagBackgroundColor = "#597EF7" ,
                    tagTextColor = "#ffffff"
                ) , Category(
                    id = 4 ,
                    sortBy = 0 ,
                    name = "Програмування, робототехніка, STEM" ,
                    description = "Вивчення природничих наук, технологій , інженерії " +
                            "та математики , STEM - освіта" ,
                    urlLogo = "/static/images/categories/programming.svg" ,
                    backgroundColor = "#597EF7" ,
                    tagBackgroundColor = "#597EF7" ,
                    tagTextColor = "#ffffff"
                )
            ) ,
            emptyList() ,
            listOf(Location("location name" , "street")) ,
            1f
        )
    )
}

@Preview
@Composable
fun ClubCardOneCategoryPreview() {
    Surface {
        ClubCard(
            Club(
                1 ,
                "Довкілля крізь призму\nукраїнської мови" ,
                description = "Американський гімнастичний клуб (American Gymnastics Club) – перша та єдина в країні мережа унікальних спортивних клубів, яка базується на Розвивальній Гімнастиці. Крім щоденних занять, в Американському гімнастичному Клубі проходять «Показові виступи» та різноманітні тематичні вечірки, які допомагають зібрати активних однодумців і популяризувати та прививати любов до спорту, перетворюючи його в стиль життя." ,
                urlLogo = "urlLogo" ,
                urlBackground = null ,
                categories = listOf(
                    Category(
                        id = 4 ,
                        sortBy = 0 ,
                        name = "Програмування, робототехніка, STEM" ,
                        description = "Вивчення природничих наук, технологій , інженерії " +
                                "та математики , STEM - освіта" ,
                        urlLogo = "/static/images/categories/programming.svg" ,
                        backgroundColor = "#597EF7" ,
                        tagBackgroundColor = "#597EF7" ,
                        tagTextColor = "#ffffff"
                    )
                ) ,
                contacts = emptyList() ,
                listOf(Location("location name" , "street")) ,
                rating = 1f
            )
        )
    }
}