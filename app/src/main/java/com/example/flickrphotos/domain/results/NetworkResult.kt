package com.example.flickrphotos.domain.results

sealed class NetworkResult<out T> {

    data class Success<T>(val response: T, val responseCode: Int) : NetworkResult<T>()
    data class Complete(val responseCode: Int) : NetworkResult<Nothing>()
    data class ApiError(val responseCode: Int, val message: String) : NetworkResult<Nothing>()

}