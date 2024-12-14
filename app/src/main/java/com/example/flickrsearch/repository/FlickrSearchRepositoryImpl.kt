package com.example.flickrsearch.repository

import com.example.flickrsearch.model.FlickrResponse
import com.example.flickrsearch.network.FlickrApiService

/**
 * Implementation of the [FlickrSearchRepository] interface.
 *
 * This class is responsible for interacting with the [FlickrApiService] to fetch image data
 * from the Flickr API based on the provided search query. It serves as the data layer between
 * the API service and the application.
 *
 * @param apiService An instance of [FlickrApiService] used to make network requests to the Flickr API.
 */
class FlickrSearchRepositoryImpl(
    private val apiService: FlickrApiService
): FlickrSearchRepository {

    /**
     * Fetches a list of images based on the given search query.
     *
     * @param query The search term to query the Flickr API for images.
     * @return A [Result] object containing a [FlickrResponse] on success or an error on failure.
     */
    override suspend fun fetchImages(query: String): Result<FlickrResponse> {
        val result = apiService.fetchImages(query = query)
        return result
    }
}