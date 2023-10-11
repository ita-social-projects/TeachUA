package com.example.teachua_android.presentation.navigation_drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.teachua_android.common.Screen

data class NavigationItem(
    val title: String,
    val Icon: ImageVector,
    val route: String
)

val navigationItemList = listOf(
    NavigationItem(
        title = "Додому",
        Icon = Icons.Filled.Home,
        route = Screen.Home.route
    ),NavigationItem(
        title = "Гуртки",
        Icon = Icons.Filled.Favorite,
        route = Screen.Clubs.route
    ),
    NavigationItem(
        title = "Челленджі" ,
        Icon = Icons.Filled.Notifications ,
        route = Screen.Challenges.route
    ) ,NavigationItem(
        title = "Про нас" ,
        Icon = Icons.Filled.Face ,
        route = Screen.AboutUs.route
    ) ,
    NavigationItem(
        title = "Послуги українською" ,
        Icon = Icons.Filled.Build ,
        Screen.ServicesInUkrainian.route
    ) ,
)