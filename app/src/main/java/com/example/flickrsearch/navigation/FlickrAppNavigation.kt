package com.example.flickrsearch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flickrsearch.model.FlickrImage
import com.example.flickrsearch.screen.FlickrDetailsScreen
import com.example.flickrsearch.screen.FlickrSearchScreen
import kotlinx.serialization.json.Json

/**
 * Sets up the navigation for the FlickrApp using Jetpack Composes Navigation component.
 *
 * This function defines a navigation host [NavHost] with two destinations:
 * 1. "search" - Displays the [FlickrSearchScreen], allowing users to search for images.
 * 2. "detail/{flickrImage}" - Displays the [FlickrDetailsScreen], showing details of a selected image.
 *
 * The navigation is managed by a [NavController], and the `flickrImage` parameter is
 * passed as a JSON string in the route arguments, then decoded into a [FlickrImage] object.
 */
@Composable
fun FlickrAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "search",
    ) {
        composable(route = "search") {
            FlickrSearchScreen(navController = navController)
        }
        composable(
            route = "detail/{flickrImage}",
        ) { backStackEntry ->
            val imageJson = backStackEntry.arguments?.getString("flickrImage") ?: ""
            val flickrImage = Json.decodeFromString<FlickrImage>(imageJson)
            FlickrDetailsScreen(
                navController = navController,
                flickrImage = flickrImage,
            )
        }
    }
}