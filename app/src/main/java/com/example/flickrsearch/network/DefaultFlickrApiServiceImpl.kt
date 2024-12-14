package com.example.flickrsearch.network

import com.example.flickrsearch.model.FlickrResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

/**
 * Default implementation of the [FlickrApiService] interface.
 *
 * @param client An instance of [HttpClient] used to perform network operations.
 *
 * This class provides the functionality to fetch image data from the Flickr API
 * by leveraging the provided HTTP client. It implements the [fetchImages] method
 * defined in the [FlickrApiService] interface.
 */
class DefaultFlickrApiServiceImpl(private val client: HttpClient): FlickrApiService {

    /**
     * Fetches images from network based on the provided search query.
     *
     * @param query The search term to query the Flickr API for images.
     * @return A [Result] object containing a [FlickrResponse] on success or an error on failure.
     */
    override suspend fun fetchImages(query: String): Result<FlickrResponse> {
        try {
            val response = client.get {
                url {
                    pathSegments = listOf(RELATIVE_PATH)
                    parameters.append(name = QUERY_PARAM_FORMAT_KEY, value = QUERY_PARAM_FORMAT_VALUE)
                    parameters.append(name = QUERY_PARAM_CALLBACK_KEY, value = QUERY_PARAM_CALLBACK_VALUE)
                    parameters.append(name = QUERY_PARAM_TAGS_KEY, value = query)
                }
            }
            return when (response.status) {
                HttpStatusCode.OK -> { Result.success(response.body<FlickrResponse>()) }
                HttpStatusCode.Unauthorized -> { Result.failure(exception = Exception("Unauthorized: Please check your API permissions.")) }
                HttpStatusCode.NotFound -> { Result.failure(exception = Exception("Not Found: The requested resource could not be found.")) }
                else -> { Result.failure(exception = Exception("Unexpected error: ${response.status.value}")) }
            }
        } catch (exception: Exception) {
            return Result.failure(exception = Exception("Unexpected error: ${exception.message}"))
        }
    }

    companion object {
        private const val RELATIVE_PATH = "feeds/photos_public.gne"
        private const val QUERY_PARAM_FORMAT_KEY = "format"
        private const val QUERY_PARAM_FORMAT_VALUE = "json"
        private const val QUERY_PARAM_CALLBACK_KEY = "nojsoncallback"
        private const val QUERY_PARAM_CALLBACK_VALUE = "1"
        private const val QUERY_PARAM_TAGS_KEY = "tags"
    }
}