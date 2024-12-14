package com.example.flickrsearch.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultFlickrApiServiceImplTest {

    private fun createMockHttpClient(responseHandler: MockRequestHandler): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler(responseHandler)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging)
        }
    }

    @Test
    fun `fetchImages returns successful result when API responds with 200`() = runTest {
        // Mock HTTP response with a 200 status and a valid FlickrResponse JSON
        val mockResponse = """
            {
                "items": [
                    {"title": "Test Image", "media": {"m": "https://api.flickr.com/test/image.png"},"author": "testAuthor","tags": "testTag","description": "testDescription"}
                ]
            }
        """.trimIndent()
        val httpClient = createMockHttpClient {
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val apiService = DefaultFlickrApiServiceImpl(httpClient)

        val result = apiService.fetchImages("test")

        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(1, response!!.items.size)
        assertEquals("Test Image", response.items[0].title)
    }

    @Test
    fun `fetchImages returns failure result when API responds with 401`() = runTest {
        val httpClient = createMockHttpClient {
            respond(
                content = "Unauthorized",
                status = HttpStatusCode.Unauthorized
            )
        }

        val apiService = DefaultFlickrApiServiceImpl(httpClient)

        val result = apiService.fetchImages("test")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Unauthorized: Please check your API permissions.", exception!!.message)
    }

    @Test
    fun `fetchImages returns failure result when API responds with 404`() = runTest {
        val httpClient = createMockHttpClient {
            respond(
                content = "Not Found",
                status = HttpStatusCode.NotFound
            )
        }

        val apiService = DefaultFlickrApiServiceImpl(httpClient)

        val result = apiService.fetchImages("test")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Not Found: The requested resource could not be found.", exception!!.message)
    }

    @Test
    fun `fetchImages returns failure result on unexpected status code`() = runTest {
        val httpClient = createMockHttpClient {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError
            )
        }

        val apiService = DefaultFlickrApiServiceImpl(httpClient)

        val result = apiService.fetchImages("test")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception!!.message!!.contains("Unexpected error"))
    }

    @Test
    fun `fetchImages returns failure result on network exception`() = runTest {
        val httpClient = createMockHttpClient {
            throw Exception("Network Error")
        }

        val apiService = DefaultFlickrApiServiceImpl(httpClient)

        val result = apiService.fetchImages("test")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception!!.message!!.contains("Unexpected error: Network Error"))
    }
}