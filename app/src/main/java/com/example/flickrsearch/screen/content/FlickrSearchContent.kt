package com.example.flickrsearch.screen.content

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.flickrsearch.R
import com.example.flickrsearch.model.FlickrImage
import com.example.flickrsearch.state.InputFieldState

/**
 * Composable function that displays a search header text when the input field is [InputFieldState.Idle].
 *
 * This header is shown with an animation using [AnimatedVisibility].
 * When the state is [InputFieldState.Idle], the header text is visible, otherwise, it is hidden.
 *
 * @param inputFieldState The current state of the input field.
 */
@Composable
fun SearchHeader(inputFieldState: InputFieldState) {
    AnimatedVisibility(visible = inputFieldState == InputFieldState.Idle) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.search_header),
            modifier = Modifier.fillMaxWidth(1f),
            style = TextStyle(
                fontWeight = FontWeight(700),
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
    }
}

/**
 * Composable function that displays a centered message when the search screen is in an idle state.
 *
 * @see stringResource for the localized string that represents the idle state message.
 */
@Composable
fun SearchScreenIdle() {
    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        DisplayText(
            text = R.string.search_screen_idle,
            textColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

/**
 * Composable function that displays a loading indicator in the center of the screen. This function
 * is used to show a `CircularProgressIndicator` while the search screen is loading data.
 */
@Composable
fun SearchScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Composable function that displays a grid of search results (Flickr images) in a vertical grid layout.
 *
 * This function takes a list of [FlickrImage] objects and displays them in a grid with 3 columns.
 * Each image is clickable, and when clicked, it triggers the [onItemClicked] callback with the clicked
 * [FlickrImage] as the argument.
 *
 * The width of each image is dynamically adjusted to fit the screen width, with each image taking up
 * one-third of the screen width.
 *
 * @param items A list of [FlickrImage] objects to be displayed in the grid.
 * @param screenWidth width of the screen.
 * @param onItemClicked lambda function that is triggered when an image item is clicked.
 */
@Composable
fun SearchResultsList(
    items: List<FlickrImage>,
    screenWidth: Float,
    onItemClicked: (FlickrImage) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3)
    ) {
        items(items) { item ->
            GridImage(
                size = (screenWidth / 3).toInt().dp,
                item = item,
                onItemClicked = onItemClicked,
            )
        }
    }
}

/**
 * Composable function that displays a grid of search results (Flickr images) in a horizontal grid layout.
 *
 * This function takes a list of [FlickrImage] objects and displays them in a grid with 2 rows.
 * Each image is clickable, and when clicked, it triggers the [onItemClicked] callback with the clicked
 * [FlickrImage] as the argument.
 *
 * The height of each image is dynamically adjusted to fit the screen height, with each image taking up
 * half of the screen height.
 *
 * @param items A list of [FlickrImage] objects to be displayed in the grid.
 * @param screenHeight height of the screen.
 * @param onItemClicked lambda function that is triggered when an image item is clicked.
 */
@Composable
fun SearchResultsListLandscape(
    items: List<FlickrImage>,
    screenHeight: Float,
    onItemClicked: (FlickrImage) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(count = 2)
    ) {
        items(items) { item ->
            GridImage(
                size = (screenHeight / 2).toInt().dp,
                item = item,
                onItemClicked = onItemClicked,
            )
        }
    }
}

/**
 * Composable function that display image fetched asynchronously using Coil.
 *
 * @param size size of the image
 * @param item data model required to extract the url for fetching the image
 * @param onItemClicked lambda function that is triggered when an image item is clicked.
 */
@Composable
fun GridImage(
    size: Dp,
    item: FlickrImage,
    onItemClicked: (FlickrImage) -> Unit,
) {
    Image(
        painter = rememberAsyncImagePainter(item.media.url),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clickable { onItemClicked(item) }
    )
}

/**
 * Composable function that displays a message when no search results are found.
 *
 * This function shows a centered text indicating that the search yielded no results.
 * The message is displayed using the [DisplayText] composable.
 */
@Composable
fun SearchScreenEmptyResult() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisplayText(
            text = R.string.search_screen_empty,
            textColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

/**
 * Composable function that displays a message when search results return an error.
 *
 * This function shows a centered text indicating that the search returned error.
 * The message is displayed using the [DisplayText] composable.
 */
@Composable
fun SearchScreenError() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisplayText(
            text = R.string.search_screen_error,
            textColor = MaterialTheme.colorScheme.onError,
        )
    }
}

/**
 * A composable function that displays a text message on the screen.
 *
 * @param text stringResource for the localized string
 * @param textColor color of the text
 */
@Composable
fun DisplayText(@StringRes text: Int, textColor: Color) {
    Text(
        modifier = Modifier.fillMaxWidth(1f).padding(horizontal = 8.dp),
        text = stringResource(text),
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            color = textColor
        ),
    )
}