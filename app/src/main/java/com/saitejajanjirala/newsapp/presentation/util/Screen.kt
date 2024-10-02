package com.saitejajanjirala.newsapp.presentation.util

sealed class Screen(val route : String) {
    object HomeScreen : Screen("home_screen")
    object DetailSreen : Screen("detail_screen")
}