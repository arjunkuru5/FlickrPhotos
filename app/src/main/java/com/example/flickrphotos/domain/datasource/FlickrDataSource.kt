package com.example.flickrphotos.domain.datasource

import com.example.flickrphotos.domain.APIService
import com.example.flickrphotos.domain.results.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class FlickrDataSource @Inject constructor(private val service: APIService) {

    suspend fun getFlickrPhotos(searchQuery: String) = callApi {
        service.getFlickrPhotos(searchQuery)
    }
}

suspend fun <T> callApi(responseBlock: suspend () -> Response<T>): NetworkResult<T> {
    val response = responseBlock()
    return if (response.isSuccessful) {
        response.body()?.let {
            NetworkResult.Success(it, response.code())
        } ?: NetworkResult.Complete(response.code())
    } else {
        NetworkResult.ApiError(response.code(), "Error!!")
    }
}