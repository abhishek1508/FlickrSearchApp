package com.example.flickrsearch.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.flickrsearch.R
import com.example.flickrsearch.model.FlickrImage
import com.example.flickrsearch.model.Media

/**
 * Composable function to display detailed information about a specific Flickr image.
 *
 * This screen displays the details of a selected Flickr image, such as its title, dimensions,
 * and author. The layout adapts to the screen orientation: in landscape mode, it uses a different
 * layout for the content display, while in portrait mode, it uses a default layout.
 * A top app bar with a back button is also included to navigate back to the previous screen.
 *
 * @param navController The navigation controller used to handle navigation actions (such as going back).
 * @param flickrImage The [FlickrImage] object containing the details of the selected image to display.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlickrDetailsScreen(
    navController: NavController,
    flickrImage: FlickrImage,
) {
    val orientation = LocalConfiguration.current.orientation
    
    Box(
        Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DetailsContentLandscape(flickrImage = flickrImage)
        } else {
            DetailsContent(flickrImage = flickrImage)
        }
        TopAppBar(
            title = {  },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent.copy(alpha = 0f),
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_content_description),
                    )
                }
            }
        )
    }
}

/**
 * Composable function to display the details of a Flickr image in landscape orientation.
 *
 * This layout is designed specifically for landscape orientation, displaying the image and its metadata
 * side by side. The image is shown on the left with a fixed width, while the metadata (such as title,
 * dimensions, and author) is displayed in a vertical column to the right. The image and metadata section
 * are aligned in a horizontal row.
 *
 * @param flickrImage The [FlickrImage] object containing the details of the selected image to display.
 */
@Composable
fun DetailsContentLandscape(flickrImage: FlickrImage) {
    Row(modifier = Modifier.fillMaxSize(1f)) {
        DetailsImage(
            flickrImage = flickrImage,
            modifier = Modifier
                .background(Color.Red)
                .fillMaxHeight()
                .width(400.dp)
        )
        Column(modifier = Modifier.fillMaxSize(1f).padding(top = 20.dp)) {
            DetailsScreenMetadata(flickrImage = flickrImage)
        }
    }
}

/**
 * Composable function to display the details of a Flickr image in portrait orientation.
 *
 * This layout is designed for portrait orientation, where the image is displayed at the top of the screen
 * with a fixed height, followed by a spacer for some separation, and then the image's metadata (such as title,
 * description, and author) is displayed below the image in a vertical column.
 *
 * @param flickrImage The [FlickrImage] object containing the details of the selected image to display.
 */
@Composable
fun DetailsContent(flickrImage: FlickrImage) {
    Column(modifier = Modifier
        .fillMaxSize(1f)
    ) {
        DetailsImage(
            flickrImage = flickrImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DetailsScreenMetadata(flickrImage = flickrImage)
    }
}

/**
 * Composable function to display metadata of a Flickr image (such as title, dimensions, and author).
 *
 * It uses regular expressions to extract the image dimensions from the description text, and it
 * displays each piece of information in a structured manner.
 *
 * @param flickrImage The [FlickrImage] object containing the details to be displayed.
 */
@Composable
fun ColumnScope.DetailsScreenMetadata(flickrImage: FlickrImage) {
    DetailsScreenText(text = stringResource(R.string.title, flickrImage.title))

    val widthHeightRegex = """width="(\d+)"\+height="(\d+)"""".toRegex()
    val matchResult = widthHeightRegex.find(flickrImage.description)
    if (matchResult != null) {
        val (width, height) = matchResult.destructured
        DetailsScreenText(text = stringResource(R.string.width, width))
        DetailsScreenText(text = stringResource(R.string.height, height))
    }
    DetailsScreenText(text = stringResource(R.string.author, flickrImage.author))
}

/**
 * Composable function to display the image from a [FlickrImage] object.
 *
 * This function uses the [Image] composable to load and display the image from the URL provided in the
 * [FlickrImage.media] property. The image is displayed with a cropping content scale to ensure it
 * fills the available space while maintaining its aspect ratio. The image is displayed using the provided
 * [Modifier] to control its layout and appearance.
 *
 * @param flickrImage The [FlickrImage] object containing the image URL to be displayed.
 * @param modifier A [Modifier] to customize the layout and styling of the image.
 */
@Composable
fun DetailsImage(flickrImage: FlickrImage, modifier: Modifier) {
    Image(
        painter = rememberAsyncImagePainter(flickrImage.media.url),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
    )
}

/**
 * Composable function to display text in a consistent style for the details screen.
 *
 * @param text The text to be displayed.
 */
@Composable
fun DetailsScreenText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        text = text,
        style = TextStyle(
            fontWeight = FontWeight(400),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
    )
}

@Preview
@Composable
fun FlickrDetailsScreenPreview() {
    FlickrDetailsScreen(
        navController = rememberNavController(),
        flickrImage = FlickrImage(
            title = "Recent Uploads tagged porcupine",
            media = Media(url = "https:\\/\\/live.staticflickr.com\\/65535\\/54186170331_2ef4588325_m.jpg"),
            author = "nobody@flickr.com (\"Fruitcake Enterprises\")",
            tags = "seattle woodlandparkzoo wildlanterns molly porcupine",
            description = "<img src=\\\"https:\\/\\/live.staticflickr.com\\/65535\\/54186170331_2ef4588325_m.jpg\\\" width=\\\"180\\\" height=\\\"240\\\" alt=\\\"12052024-30\\\" \\/>"
        )
    )
}