package com.example.teachua_android.presentation.clubs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teachua_android.R.*
import com.example.teachua_android.presentation.clubs.components.ClubCard
import com.example.teachua_android.presentation.home.components.HomeFooter
import com.example.teachua_android.presentation.home.maxToolbarHeight
import com.example.teachua_android.presentation.home.minToolbarHeight
import com.example.teachua_android.presentation.home.rememberToolbarState
import com.example.teachua_android.presentation.navigation_drawer.navigationItemList
import com.example.teachua_android.presentation.toolbar.CollapsingToolbar
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

    val scrollState = rememberScrollState()

    val toolbarHeightRange = with(LocalDensity.current) {
        minToolbarHeight.roundToPx()..maxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    toolbarState.scrollValue = scrollState.value

    ModalNavigationDrawer(
        drawerContent = {
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
        } ,
        drawerState = drawerState ,
        modifier = Modifier.wrapContentSize()) {
        Column(modifier = modifier) {
            CollapsingToolbar(
                backgroundImageResId = drawable.header_main_bg ,
                progress = toolbarState.progress ,
                onMenuClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                } ,
                onAddClubClicked = { /*TODO*/ } ,
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                    .graphicsLayer { translationY = toolbarState.offset }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally ,
                verticalArrangement = Arrangement.spacedBy(20.dp) ,
                modifier = modifier.verticalScroll(scrollState) ,
            ) {
                Spacer(modifier = modifier.height(PaddingValues(top = 8.dp/*maxToolbarHeight*/).calculateTopPadding()))

                Text(text = "${viewModel.clubsState.value.clubs.size}")

                viewModel.clubsState.value.clubs.take(5).forEach { club ->

                    ClubCard(club = club)
                }

                HomeFooter(onHelpButtonClick = { /*TODO*/ })
            }
        }

    }
}






