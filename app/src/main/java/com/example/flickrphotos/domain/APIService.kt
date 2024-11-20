package com.example.flickrphotos.domain

import com.example.flickrphotos.domain.models.FlickrFeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getFlickrPhotos(@Query("tags") tags: String): Response<FlickrFeedResponse>
}