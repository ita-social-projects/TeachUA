package com.example.teachua_android.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teachua_android.R
import com.example.teachua_android.presentation.cities.CitiesViewModel
import com.example.teachua_android.presentation.home.components.HomePageContent
import com.example.teachua_android.presentation.navigation_drawer.navigationItemList
import com.example.teachua_android.presentation.toolbar.CollapsingToolbar
import com.example.teachua_android.presentation.toolbar.ToolbarState
import com.example.teachua_android.presentation.toolbar.scrollflags.ExitUntilCollapsedState
import kotlinx.coroutines.launch

val minToolbarHeight = 72.dp
val maxToolbarHeight = 210.dp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController ,
    modifier: Modifier = Modifier ,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = navigationItemList.indexOfFirst { it.route == currentRoute }

    val scrollState = rememberScrollState()

    val toolbarHeightRange = with(LocalDensity.current) {
        minToolbarHeight.roundToPx()..maxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    toolbarState.scrollValue = scrollState.value



    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(16.dp))
            navigationItemList.forEachIndexed { index , item ->
                NavigationDrawerItem(label = {
                    Text(text = item.title)
                } , selected = index == selectedItemIndex , onClick = {
                    navController.navigate(item.route) {

                    }
                    scope.launch {
                        drawerState.close()
                    }
                } , icon = {
                    Icon(
                        imageVector = item.Icon , contentDescription = item.title
                    )
                } , modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))
            }
        }
    } , drawerState = drawerState , modifier = modifier.wrapContentSize()) {

        Box(modifier = modifier) {
            HomePageContent(
                navController = navController ,
                modifier = Modifier
                    .verticalScroll(scrollState),
                contentPadding = PaddingValues(top = maxToolbarHeight)
            )
            CollapsingToolbar(
                backgroundImageResId = R.drawable.header_main_bg ,
                progress = toolbarState.progress ,
                onMenuClicked = { scope.launch {
                    drawerState.open()
                } } ,
                onAddClubClicked = { /*TODO*/ } ,
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                    .graphicsLayer { translationY = toolbarState.offset }
            )
        }
    }

}

@Composable
fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}


@Composable
fun LocationFragment(
    modifier: Modifier = Modifier ,
    viewModel: CitiesViewModel = hiltViewModel() ,
) {
    var popupControl by remember {
        mutableStateOf(false)
    }
    val color = MaterialTheme.colorScheme.onPrimary
    val cities = viewModel.citiesState.cities
    val currentCity = viewModel.citiesState.currentCity
    Row(
        modifier = modifier
            .padding(3.dp)
            .clickable(onClick = { popupControl = true }) ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (popupControl) {
            Popup(onDismissRequest = {
                popupControl = false
            }) {
                Surface(
                    shape = RoundedCornerShape(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(cities) { city ->
                            Text(text = city.name ,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        viewModel.citiesState.currentCity = city
                                        popupControl = false

                                    })

                        }
                    }
                }
            }
        }
        Icon(
            imageVector = Icons.Default.LocationOn ,
            contentDescription = null ,
            tint = color ,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = currentCity?.name ?: "Київ" ,
            color = color ,
            fontSize = 14.sp ,
            fontFamily = FontFamily.SansSerif ,
            fontWeight = FontWeight.Medium ,
            textAlign = TextAlign.Right ,
            modifier = Modifier.padding(start = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown ,
            contentDescription = "Location dropdown arrow" ,
            tint = color ,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

}

@Composable
fun UnderlinedTextInput(
    value: String , onSearch: (String) -> Unit , modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(
        fontSize = 16.sp ,
        color = MaterialTheme.colorScheme.onPrimary ,
        fontWeight = FontWeight.Light ,
        fontFamily = FontFamily.SansSerif ,
    )
    var textValue by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 7.dp)
            ) {
                if (textValue.isEmpty()) {
                    Text(value , style = textStyle)
                }

                BasicTextField(value = textValue , onValueChange = {
                    textValue = it
                } , textStyle = textStyle , keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ) , keyboardActions = KeyboardActions(onDone = {
                    onSearch(textValue)
                }) , singleLine = true)
            }

            Icon(
                imageVector = Icons.Default.Search ,
                contentDescription = "Search clubs" ,
                tint =  MaterialTheme.colorScheme.onPrimary ,
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
                .background(MaterialTheme.colorScheme.onPrimary)
        )
    }
}

