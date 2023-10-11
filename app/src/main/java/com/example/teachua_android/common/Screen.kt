package com.example.teachua_android.common

sealed class Screen(val route: String){
    object Home : Screen("home")
    object Clubs : Screen("clubs")
    object Challenges : Screen("challenges")
    object AboutUs: Screen("about_us")
    object ServicesInUkrainian: Screen("services_in_ukrainian")
}
