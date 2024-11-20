package com.example.flickrphotos.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "image_search") {
        composable("image_search") { ImageGridScreen(navController = navController) }
        composable("image_details") { ImageDetailsScreen(navController = navController) }
    }
}