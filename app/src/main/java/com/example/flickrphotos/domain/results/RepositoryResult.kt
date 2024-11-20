package com.example.flickrphotos.domain.results

sealed class RepositoryResult<out T> {
    data class Success<T>(val response: T) : RepositoryResult<T>()
    data object Complete : RepositoryResult<Nothing>()
    data object Error : RepositoryResult<Nothing>()
}