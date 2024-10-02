package com.saitejajanjirala.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saitejajanjirala.newsapp.presentation.util.Screen
import com.saitejajanjirala.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme (dynamicColor = true) {
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.surface
               ) {
                   val navController = rememberNavController()
                   NavHost(navController, startDestination = Screen.HomeScreen.route){
                       composable(Screen.HomeScreen.route){
                           HomeScreen(navController)
                       }
                       composable(Screen.DetailSreen.route+"?url={url}",
                           arguments = listOf(
                               navArgument(
                                   "url"
                               ){
                                   type = NavType.StringType
                                   defaultValue = ""
                               }
                           ),){
                           DetailScreen(navController)
                       }
                   }

               }
            }
        }
    }
}
