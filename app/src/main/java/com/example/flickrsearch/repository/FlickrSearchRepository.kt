package com.example.flickrsearch.repository

import com.example.flickrsearch.model.FlickrResponse

/**
 * Repository interface for searching and retrieving images from the Flickr API.
 *
 * This abstraction separates the data access logic from other parts of the application,
 * allowing for easier testing and maintenance.
 */
interface FlickrSearchRepository {

    /**
     * Fetches a list of images based on the given search query.
     *
     * @param query The search term to query the Flickr API for images.
     * @return A [Result] object containing a [FlickrResponse] on success or an error on failure.
     */
    suspend fun fetchImages(query: String): Result<FlickrResponse>
}