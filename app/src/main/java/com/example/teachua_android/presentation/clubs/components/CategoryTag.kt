package com.example.teachua_android.presentation.clubs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.teachua_android.domain.model.club.Category

@Composable
fun CategoryTag(category: Category , modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp , Alignment.Start) ,
        verticalAlignment = Alignment.CenterVertically ,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                try {
                    Color(category.tagBackgroundColor.toColorInt())
                } catch (e: java.lang.IllegalArgumentException) {
                    Color.White
                }
            )
    ) {
        Icon(
            Icons.Default.Add , null , tint = try {
                Color(category.tagTextColor.toColorInt())
            } catch (e: java.lang.IllegalArgumentException) {
                Color.White
            }
        )
        Text(
            text = category.name ,
            color = try {
                Color(category.tagTextColor.toColorInt())
            } catch (e: java.lang.IllegalArgumentException) {
                Color.White
            } ,
            overflow = TextOverflow.Ellipsis ,
            maxLines = 1 ,
            modifier = Modifier.widthIn(max = 140.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Preview
@Composable
fun CategoryTagPreview() {
    CategoryTag(
        category =
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
    )

}