package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.presentation.ui.theme.SubtextGray
import com.example.teachua_android.presentation.ui.theme.TitleBlue

@Composable
fun TitleText(
    title: String,
    subtext: String,
    titleFontSize: TextUnit,
    titleLineHeight: TextUnit,
    modifier: Modifier = Modifier,
    alignCenter: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = title, style = TextStyle(
                fontSize = titleFontSize,
                lineHeight = titleLineHeight,
                fontWeight = FontWeight(700),
                color = TitleBlue,
                textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if (subtext.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = subtext, style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400),
                    color = SubtextGray,
                    textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
                )
            )
        }
    }
}

@Preview(backgroundColor = 0xFFECDBDB, showSystemUi = true, showBackground = true)
@Composable
fun TitleTextPreview() {
    Surface {
        TitleText(
            title = "Про нас",
            subtext = "Ініціатива \"Навчай українською\" - це небайдужі громадяни, " +
                    "які об'єдналися, щоб популяризувати українську мову у сфері освіти.\n",
            titleFontSize = 24.sp,
            titleLineHeight = 32.sp,
            alignCenter = true
        )
    }
}