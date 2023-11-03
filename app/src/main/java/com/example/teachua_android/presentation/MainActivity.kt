package com.example.teachua_android.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teachua_android.common.Screen
import com.example.teachua_android.presentation.clubs.ClubsPage
import com.example.teachua_android.presentation.home.HomePage
import com.example.teachua_android.presentation.navigation_drawer.NotImplementedScreen
import com.example.teachua_android.presentation.ui.theme.TeachUA_androidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeachUA_androidTheme {
                val navController = rememberNavController()
                NavHost(navController = navController , startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomePage(navController = navController)
                    }
                    composable(Screen.AboutUs.route) {
                        NotImplementedScreen()
                    }
                    composable(Screen.ServicesInUkrainian.route) {
                        NotImplementedScreen()
                    }
                    composable(Screen.Challenges.route) {
                        NotImplementedScreen()
                    }
                    composable(Screen.Clubs.route) {
                        ClubsPage(navController = navController)
                    }

                }
            }
        }
    }
}
