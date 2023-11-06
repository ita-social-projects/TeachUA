package com.example.teachua_android.presentation.toolbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.teachua_android.R
import com.example.teachua_android.presentation.home.LocationFragment
import com.example.teachua_android.presentation.home.UnderlinedTextInput
import com.example.teachua_android.presentation.home.components.OrangeButtonWithWhiteText
import com.example.teachua_android.presentation.ui.theme.TeachUA_androidTheme

private val contentPadding = 16.dp
private val elevation = 4.dp

private val menuIconHeight = 42.dp


@Preview
@Composable
fun CollapsingToolbarCollapsedPreview() {
    TeachUA_androidTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.header_main_bg ,
            progress = 0f ,
            onMenuClicked = {} ,
            onAddClubClicked = {} ,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarHalfwayPreview() {
    TeachUA_androidTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.header_main_bg ,
            progress = 0.5f ,
            onMenuClicked = {} ,
            onAddClubClicked = {} ,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarExpandedPreview() {
    TeachUA_androidTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.header_main_bg ,
            progress = 1f ,
            onMenuClicked = {} ,
            onAddClubClicked = {} ,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )
    }
}

@Composable
fun CollapsingToolbar(
    @DrawableRes backgroundImageResId: Int ,
    progress: Float ,
    onMenuClicked: () -> Unit ,
    onAddClubClicked: () -> Unit ,
    modifier: Modifier = Modifier
) {

    Surface(
        color = MaterialTheme.colors.primary ,
        elevation = elevation ,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = backgroundImageResId) ,
                contentDescription = null ,
                contentScale = ContentScale.FillBounds ,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = progress
                    } ,
            )
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = contentPadding)
                    .fillMaxSize() ,
                contentAlignment = Alignment.Center
            ) {
                CollapsingToolbarLayout(progress = progress) {
                    IconButton(
                        onClick = { onMenuClicked() } ,
                        modifier = Modifier
                            .height(menuIconHeight)
                            .wrapContentWidth() ,
                    ) {

                        Icon(
                            Icons.Default.Menu ,
                            contentDescription = null ,
                            tint =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    LocationFragment()

                    OrangeButtonWithWhiteText(
                        text = "Додати гурток" ,
                        onClick = { onAddClubClicked() } ,
                    )

                    if (progress != 0f)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    alpha = progress
                                } ,
                            horizontalArrangement = Arrangement.SpaceBetween ,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Гуртки у місті Київ" ,
                                fontSize = 24.sp ,
                                fontWeight = FontWeight(700) ,
                                color =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimary ,

                                )
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.LocationOn ,
                                contentDescription = null ,
                                tint =  androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    else {
                        Box {

                        }
                    }

                    if (progress != 0f)
                        UnderlinedTextInput(
                            value = stringResource(R.string.which_club_are_you_searching) ,
                            onSearch = { } ,
                            Modifier.graphicsLayer {
                                alpha = progress
                            }
                        ) else {
                        Box {

                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun CollapsingToolbarLayout(
    progress: Float ,
    modifier: Modifier = Modifier ,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier ,
        content = content
    ) { measurables , constraints ->
        check(measurables.size == 5)

        val placeables = measurables.map {
            it.measure(constraints)
        }
        layout(
            width = constraints.maxWidth ,
            height = constraints.maxHeight
        ) {

            val menu = placeables[0]
            val location = placeables[1]

            val buttons = placeables[2]
            val clubsInLocation = placeables[3]

            val textInput = placeables[4]

            menu.placeRelative(
                x = 0 ,
                y = buttons.height / 4
            )

            location.placeRelative(
                x = (constraints.maxWidth / 2) - location.width ,
                y = buttons.height / 4 + (buttons.height - location.height) / 2
            )

            buttons.placeRelative(
                x = constraints.maxWidth - buttons.width ,
                y = buttons.height / 4
            )

            clubsInLocation.placeRelative(
                x = 0 ,
                y = lerp(
                    start = buttons.height / 4 + buttons.height,
                    stop = buttons.height * 2,
                    fraction = progress
                )
            )

            textInput.placeRelative(
                x = 0 ,
                y = lerp(
                    start = buttons.height / 4 + buttons.height ,
                    stop = constraints.maxHeight - textInput.height - buttons.height /2 ,
                    fraction = progress
                )
            )
        }
    }
}