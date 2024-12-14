package com.example.flickrsearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the top-level response from the Flickr API.
 *
 * @property items A list of [FlickrImage] objects, each representing an image in the response.
 */
@Serializable
data class FlickrResponse(
    @SerialName("items")
    val items: List<FlickrImage>
)

/**
 * Represents a single image item in the Flickr API response.
 *
 * @property title The title of the image.
 * @property media An instance of [Media] containing the image URL.
 * @property author The author of the image.
 * @property tags A comma-separated list of tags associated with the image.
 * @property description A description of the image in HTML format.
 */
@Serializable
data class FlickrImage(
    @SerialName("title")
    val title: String,
    @SerialName("media")
    val media: Media,
    @SerialName("author")
    val author: String,
    @SerialName("tags")
    val tags: String,
    @SerialName("description")
    val description: String,
)

/**
 * Represents the media details for a Flickr image.
 *
 * @property url The URL of the image.
 */
@Serializable
data class Media(
    @SerialName("m")
    val url: String,
)
