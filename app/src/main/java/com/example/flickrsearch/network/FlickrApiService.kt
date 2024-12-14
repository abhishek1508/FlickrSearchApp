package com.example.flickrsearch.network

import com.example.flickrsearch.model.FlickrResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.AndroidEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Interface defining the Flickr API service contract.
 *
 * This service is used to fetch image data from the Flickr API using an HTTP client.
 */
interface FlickrApiService {

    /**
     * Fetches images based on the provided search query.
     *
     * @param query The search term to query the Flickr API for images.
     * @return A [Result] object containing a [FlickrResponse] on success or an error on failure.
     */
    suspend fun fetchImages(query: String): Result<FlickrResponse>

    companion object {
        private const val HOST = "api.flickr.com/services"

        /**
         * Creates and configures an instance of [HttpClient] for interacting with the Flickr API.
         *
         * The client is set up with:
         * - Default requests pointing to the Flickr API host with HTTPS protocol.
         * - Logging for detailed HTTP activity.
         * - Content negotiation to parse JSON responses, ignoring unknown keys for flexibility.
         *
         * @return A configured [HttpClient] instance.
         */
        fun createClient(engineFactory: HttpClientEngineFactory<AndroidEngineConfig>): HttpClient {
            return HttpClient(engineFactory = engineFactory) {
                defaultRequest {
                    host = HOST
                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }

                install(Logging) {
                    level = LogLevel.ALL
                }

                install(ContentNegotiation) {
                    json(
                      Json {
                          ignoreUnknownKeys = true
                          prettyPrint = true
                      }
                    )
                }
            }
        }
    }
}