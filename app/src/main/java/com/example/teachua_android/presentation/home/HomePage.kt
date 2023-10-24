package com.example.teachua_android.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teachua_android.R
import com.example.teachua_android.presentation.home.components.HomePageContent
import com.example.teachua_android.presentation.home.components.OrangeButtonWithWhiteText
import com.example.teachua_android.presentation.navigation_drawer.navigationItemList
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController ,
    modifier: Modifier = Modifier ,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = navigationItemList.indexOfFirst { it.route == currentRoute }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val scrollState = rememberScrollState()

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
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) ,
            topBar = {
                Box(
//                     modifier = modifier.heightIn(max = 56.dp) ,
                ) {
                    /*HomeHeaderContent(onAddClubsButtonClick = {} ,
                        location = "Київ" ,
                        onLocationChange = {} ,
                        onSearch = {} ,
                        modifier = Modifier)*/

                    CenterAlignedTopAppBar(title = {
                        Column {
                            LocationFragment()
                        }
                    } ,
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu ,
                                    contentDescription = "Localized description" ,
                                    tint = Color.White
                                )
                            }
                        } ,
                        actions = {
                            OrangeButtonWithWhiteText(
                                text = "Додати гурток" ,
                                onClick = { } ,
                            )
                        } ,
                        scrollBehavior = scrollBehavior ,
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary))
                }
            }) { padding ->
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                HomePageContent(Modifier.padding(padding))
            }
        }

    }
}

@Composable
fun HomeHeaderContent(
    location: String = "Київ" ,
    onAddClubsButtonClick: () -> Unit ,
    onLocationChange: () -> Unit ,
    onSearch: (String) -> Unit ,
    modifier: Modifier = Modifier ,
) {
    Box(
        modifier = modifier.heightIn(max = 200.dp) , contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.header_main_bg) ,
            contentDescription = null ,
            contentScale = ContentScale.Crop ,
            modifier = Modifier
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight() ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth() ,
                horizontalArrangement = Arrangement.SpaceBetween ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Гуртки у місті $location" ,
                    fontSize = 24.sp ,
                    fontWeight = FontWeight(700) ,
                    color = Color.White ,

                    )
                Icon(
                    imageVector = Icons.Default.LocationOn ,
                    contentDescription = null ,
                    tint = Color.White
                )
            }
            UnderlinedTextInput(
                value = stringResource(R.string.which_club_are_you_searching) , onSearch = onSearch
            )
        }
    }
}

@Composable
fun LocationFragment(
    modifier: Modifier = Modifier ,
    viewModel: HomePageViewModel = hiltViewModel() ,
) {
    var popupControl by remember {
        mutableStateOf(false)
    }
    val color = MaterialTheme.colorScheme.onPrimary
    val cities = viewModel.citiesState.value.cities
    var currentCity = viewModel.citiesState.value.currentCity

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
                                        viewModel.citiesState.value.currentCity = city
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
            text = currentCity?.name ?: "" ,
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
                tint = Color.White ,
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


@Preview(showBackground = false)
@Composable
fun MainHeader2Preview() {
    HomeHeaderContent(onAddClubsButtonClick = {} ,
        location = "Київ" ,
        onLocationChange = {} ,
        onSearch = {} ,
        modifier = Modifier)
}

