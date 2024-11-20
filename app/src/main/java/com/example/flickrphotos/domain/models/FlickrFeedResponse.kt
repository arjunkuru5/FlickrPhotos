package com.example.flickrphotos.domain.models

import kotlinx.serialization.SerialName

data class FlickrFeedResponse(
    @SerialName("items")
    val items: List<Item>
)

data class Item(
    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("author")
    val author: String,

    @SerialName("published")
    val published: String,

    @SerialName("media")
    val media: Media
)

data class Media(
    @SerialName("m")
    val m: String
)