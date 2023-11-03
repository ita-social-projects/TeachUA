package com.example.teachua_android.presentation.clubs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teachua_android.R.*
import com.example.teachua_android.presentation.clubs.components.ClubCard
import com.example.teachua_android.presentation.home.LocationFragment
import com.example.teachua_android.presentation.home.components.HomeFooter
import com.example.teachua_android.presentation.home.components.OrangeButtonWithWhiteText
import com.example.teachua_android.presentation.navigation_drawer.navigationItemList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubsPage(
    navController: NavController , modifier: Modifier = Modifier ,
    viewModel: ClubsViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = navigationItemList.indexOfFirst { it.route == currentRoute }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val lazyListState = rememberLazyListState()


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                navigationItemList.forEachIndexed { index , item ->
                    NavigationDrawerItem(label = {
                        androidx.compose.material3.Text(text = item.title)
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
        } ,
        drawerState = drawerState ,
        modifier = Modifier.wrapContentSize()) {
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() ,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(id = drawable.home_page_bg) ,
                    contentDescription = null ,
                    contentScale = ContentScale.FillBounds ,
                    modifier = Modifier.matchParentSize()
                )
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = modifier.padding(padding)
                ){
                    items(viewModel.clubsState.value.clubs.take(5)){
                        ClubCard(club = it)
                    }

                    item {
                        HomeFooter(onHelpButtonClick = { /*TODO*/ })
                    }
                }

            }

        }

    }
}



