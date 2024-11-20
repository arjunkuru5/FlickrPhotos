package com.example.flickrphotos.domain.usecase

import com.example.flickrphotos.domain.repository.FlickrRepository
import com.example.flickrphotos.domain.results.RepositoryResult
import com.example.flickrphotos.domain.results.UseCaseResult
import com.example.flickrphotos.ui.model.FlickrPhoto
import javax.inject.Inject

class FlickrUseCase @Inject constructor(private val repository: FlickrRepository) {

    suspend fun getFlickrPhotos(searchQuery: String): UseCaseResult<List<FlickrPhoto>> {
        return repository.getFlickrPhotos(searchQuery).toUseCaseResult(mapper = {
            it.items.map { item ->
                FlickrPhoto(
                    title = item.title,
                    description = item.description,
                    author = item.author,
                    publishedDate = item.published,
                    image = item.media.m
                )
            }
        })
    }
}

fun <T, R> RepositoryResult<T>.toUseCaseResult(mapper: (T) -> R): UseCaseResult<R> = when (this) {
    is RepositoryResult.Success -> UseCaseResult.Success(mapper(this.response))
    is RepositoryResult.Complete -> UseCaseResult.Completed
    is RepositoryResult.Error -> UseCaseResult.Error
}
