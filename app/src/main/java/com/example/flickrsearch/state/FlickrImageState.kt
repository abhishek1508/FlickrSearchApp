package com.example.flickrsearch.state

import com.example.flickrsearch.model.FlickrResponse

/**
 * Sealed interface representing the various states of the Flickr image search.
 *
 * The sealed interface provides a type-safe way to handle each state by restricting the possible states
 * that the [FlickrImageState] can have, ensuring that the UI can handle each state explicitly.
 */
sealed interface FlickrImageState {

    /**
     * Represents the idle state when no search is in progress and no results are displayed.
     */
    data object Idle : FlickrImageState

    /**
     * Represents the loading state while images are being fetched from the API.
     */
    data object Loading : FlickrImageState

    /**
     * Represents the state when no results are found for the search query.
     */
    data object EmptyResults : FlickrImageState

    /**
     * Represents the error state when an error occurs during the image fetch process.
     *
     * @param message Optional error message providing details about the error.
     */
    data class Error(val message: String?) : FlickrImageState

    /**
     * Represents the state when search results are successfully fetched and are available.
     *
     * @param response The [FlickrResponse] containing the list of fetched images.
     */
    data class Content(val response: FlickrResponse) : FlickrImageState
}