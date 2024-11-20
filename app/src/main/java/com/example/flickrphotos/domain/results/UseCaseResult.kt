package com.example.flickrphotos.domain.results

sealed class UseCaseResult<out T> {
    data class Success<T>(val data: T) : UseCaseResult<T>()
    data object Completed : UseCaseResult<Nothing>()
    data object Error : UseCaseResult<Nothing>()
}