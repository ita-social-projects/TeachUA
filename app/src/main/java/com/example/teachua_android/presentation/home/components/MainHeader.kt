package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.R

@Composable
fun MainHeader(
    location: String = "Київ",
    onMenuButtonClick: () -> Unit,
    onAddClubsButtonClick: () -> Unit,
    onLocationChange: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    isAuthorized: Boolean = false
) {
    Box(
        modifier = modifier.heightIn(max = 225.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.header_main_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HomeToolbar(
                location = location,
                onMenuButtonClick = onMenuButtonClick,
                onAddClubsButtonClick = onAddClubsButtonClick,
                isAuthorized = isAuthorized,
                onLocationChange = onLocationChange
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Гуртки у місті $location",
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onPrimary,

                    )
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            UnderlinedTextInput(
                value = stringResource(R.string.which_club_are_you_searching),
                onSearch = onSearch
            )
        }
    }
}

@Composable
fun HomeToolbar(
    location: String,
    onMenuButtonClick: () -> Unit,
    onAddClubsButtonClick: () -> Unit,
    onLocationChange: () -> Unit,
    modifier: Modifier = Modifier,
    isAuthorized: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(32.dp)
                .clickable(onClick = onMenuButtonClick)
        )
        Spacer(modifier = Modifier.weight(4f))
        LocationFragment(location = location, onLocationChange = onLocationChange)
        Spacer(modifier = Modifier.weight(1f))
        if (!isAuthorized) {
            OrangeButton(text = "Додати гурток", onClick = onAddClubsButtonClick)
        }
    }
}

@Composable
fun LocationFragment(
    location: String,
    onLocationChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.onPrimary
    Row(
        modifier = modifier
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = location,
            color = color,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Right,
            modifier = Modifier.padding(start = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Location dropdown arrow",
            tint = color,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(onClick = onLocationChange)
        )
    }

}

@Composable
fun UnderlinedTextInput(
    value: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Light,
        fontFamily = FontFamily.SansSerif,
    )
    var textValue by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(modifier = Modifier
                .weight(1f)
                .padding(bottom = 7.dp)) {
                if (textValue.isEmpty()) {
                    Text(value, style = textStyle)
                }

                BasicTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                    },
                    textStyle = textStyle,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSearch(textValue)
                        }
                    ),
                    singleLine = true
                )
            }

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search clubs",
                tint = Color.White,
                modifier = Modifier.clickable(onClick = {
                    onSearch(textValue)
                })
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LocationFragmentPreview() {
    Surface(color = Color.Black)
    {
        LocationFragment(location = "Київ", onLocationChange = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFE43434)
@Composable
fun HomeToolbarPreview() {
    HomeToolbar(
        onMenuButtonClick = { /*TODO*/ },
        onAddClubsButtonClick = {},
        location = "Київ",
        onLocationChange = {})
}

@Preview(showBackground = false)
@Composable
fun MainHeader2Preview() {
    MainHeader(
        onMenuButtonClick = { /*TODO*/ },
        onAddClubsButtonClick = {},
        location = "Київ",
        onLocationChange = {},
        onSearch = {},
        modifier = Modifier)
}

