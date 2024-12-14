package com.example.flickrsearch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flickrsearch.navigation.FlickrAppNavigation
import com.example.flickrsearch.ui.theme.FlickrSearchTheme

/**
 * MainActivity is the entry point of the application.
 * It sets up the Compose content and initializes the navigation graph.
 */
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickrSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    FlickrAppNavigation()
                }
            }
        }
    }
}

// See if we want to add Room

// Extra points
// Share functionality
// Support landscape
// Unit testing

