package com.example.flickrphotos.domain.repository

import com.example.flickrphotos.domain.datasource.FlickrDataSource
import com.example.flickrphotos.domain.models.FlickrFeedResponse
import com.example.flickrphotos.domain.results.NetworkResult
import com.example.flickrphotos.domain.results.RepositoryResult
import javax.inject.Inject

class FlickrRepository @Inject constructor(private val dataSource: FlickrDataSource) {

    suspend fun getFlickrPhotos(searchQuery: String): RepositoryResult<FlickrFeedResponse> {
        return dataSource.getFlickrPhotos(searchQuery).toRepositoryResult()
    }
}


fun <T> NetworkResult<T>.toRepositoryResult(): RepositoryResult<T> =
    when (this) {
        is NetworkResult.Success -> RepositoryResult.Success(this.response)
        is NetworkResult.Complete -> RepositoryResult.Complete
        is NetworkResult.ApiError -> RepositoryResult.Error
    }