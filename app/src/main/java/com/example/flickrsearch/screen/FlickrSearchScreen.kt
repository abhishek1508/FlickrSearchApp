package com.example.flickrsearch.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flickrsearch.component.SearchableInputField
import com.example.flickrsearch.model.FlickrImage
import com.example.flickrsearch.screen.content.SearchHeader
import com.example.flickrsearch.screen.content.SearchResultsList
import com.example.flickrsearch.screen.content.SearchResultsListLandscape
import com.example.flickrsearch.screen.content.SearchScreenEmptyResult
import com.example.flickrsearch.screen.content.SearchScreenError
import com.example.flickrsearch.screen.content.SearchScreenIdle
import com.example.flickrsearch.screen.content.SearchScreenLoading
import com.example.flickrsearch.state.FlickrImageState
import com.example.flickrsearch.state.InputFieldState
import com.example.flickrsearch.viewmodel.FlickrSearchViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder

/**
 * Composable function that represents the Flickr search screen.
 *
 * This screen allows users to search for images and displays the results.
 *
 * The screen layout is rendered using the [SearchScreenLayout] composable, passing various states
 * such as the current search query, image results, and input field state. It also provides callbacks
 * to handle search query changes, input field interactions (click, clear), and item selection.
 *
 * When an image item is clicked, the selected item is serialized into a JSON string, which is then
 * URL-encoded and passed through navigation to the image detail screen.
 *
 * @param navController The navigation controller used to navigate between screens.
 */
@Composable
fun FlickrSearchScreen(navController: NavController) {
    val viewModel = koinViewModel<FlickrSearchViewModel>()

    val viewState = viewModel.flickrImageState.collectAsState().value
    val searchFieldState = viewModel.inputFieldState.collectAsState().value
    val searchQuery = viewModel.searchQuery.collectAsState().value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val orientation = LocalConfiguration.current.orientation

    SearchScreenLayout(
        orientation = orientation,
        viewState = viewState,
        inputFieldState = searchFieldState,
        screenWidth = screenWidth.value,
        screenHeight = screenHeight.value,
        searchQuery = searchQuery,
        onSearchInputChanged = { query -> viewModel.updateSearchQuery(query) },
        onSearchInputClicked = { viewModel.onSearchInputFieldClicked() },
        onClearInputClicked = { viewModel.onSearchInputFieldCleared() },
        onItemClicked = { item ->
            val jsonString = Json.encodeToString<FlickrImage>(item)
            // Navigation routes are equivalent to urls. When you need to pass a URL inside another URL you need to encode the URLs
            val encoder = URLEncoder.encode(jsonString, "UTF-8")
            navController.navigate("detail/$encoder")
        }
    )
}

/**
 * Composable function that defines the layout and behavior of the search screen.
 *
 * This layout consists of a header, an input field, and different sections based on the current
 * state of the search (idle, loading, content, empty results, or error). It handles displaying
 * the appropriate content based on the `viewState` and manages interactions with the search input
 * field and item clicks.
 *
 * @param orientation The current orientation of the device i.e. PORTRAIT or LANDSCAPE
 * @param viewState [FlickrImageState] to display, determining the content shown
 * @param inputFieldState The current state of [InputFieldState].
 * @param screenWidth The width of the screen.
 * @param screenHeight The height of the screen.
 * @param searchQuery The current search query text entered by the user.
 * @param onSearchInputChanged A lambda to handle changes to the search query.
 * @param onSearchInputClicked A lambda to handle clicks on the search input field.
 * @param onClearInputClicked A lambda to handle clearing the search input field.
 * @param onItemClicked A lambda to handle clicks on individual search result items.
 */
@Composable
private fun SearchScreenLayout(
    orientation: Int,
    viewState: FlickrImageState,
    inputFieldState: InputFieldState,
    screenWidth: Float,
    screenHeight: Float,
    searchQuery: String,
    onSearchInputChanged: (String) -> Unit,
    onSearchInputClicked: () -> Unit,
    onClearInputClicked: () -> Unit,
    onItemClicked: (FlickrImage) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        SearchHeader(inputFieldState = inputFieldState)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        SearchableInputField (
            inputFieldState = inputFieldState,
            searchQuery = searchQuery,
            onClearInputClicked = onClearInputClicked,
            onSearchInputChanged = onSearchInputChanged,
            onSearchInputClicked = onSearchInputClicked,
        )
        when (viewState) {
            FlickrImageState.Idle -> {
                SearchScreenIdle()
            }
            FlickrImageState.Loading -> {
                SearchScreenLoading()
            }
            is FlickrImageState.Content -> {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    SearchResultsListLandscape(
                        items = viewState.response.items,
                        screenHeight = screenHeight,
                        onItemClicked = onItemClicked,
                    )
                } else {
                    SearchResultsList(
                        items = viewState.response.items,
                        screenWidth = screenWidth,
                        onItemClicked = onItemClicked,
                    )
                }
            }
            is FlickrImageState.EmptyResults -> {
                SearchScreenEmptyResult()
            }
            is FlickrImageState.Error -> {
                SearchScreenError()
            }
        }
    }
}

@Preview
@Composable
fun FlickrSearchScreenPreview() {
    FlickrSearchScreen(navController = rememberNavController())
}