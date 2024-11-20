package com.example.flickrphotos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrphotos.domain.results.UseCaseResult
import com.example.flickrphotos.domain.usecase.FlickrUseCase
import com.example.flickrphotos.ui.model.FlickrPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainViewModelState {
    data object Init : MainViewModelState
    data object Loading : MainViewModelState
    data class ImagesLoaded(val images: List<FlickrPhoto>) : MainViewModelState
    data object Error : MainViewModelState
}

sealed interface MainViewModelIntent {
    data class LoadImagesFromQuery(val searchQuery: String) : MainViewModelIntent
}

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: FlickrUseCase) : ViewModel() {

    private var sharedFlow = MutableSharedFlow<MainViewModelState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    fun getSharedFlow(): Flow<MainViewModelState> = sharedFlow.map { it }

    fun sendIntent(intent: MainViewModelIntent) {
        when (intent) {
            is MainViewModelIntent.LoadImagesFromQuery -> loadImages(intent.searchQuery)
        }
    }

    private fun loadImages(searchQuery: String) {
        viewModelScope.launch {
            sharedFlow.tryEmit(MainViewModelState.Loading)
            when (val result = useCase.getFlickrPhotos(searchQuery)) {
                is UseCaseResult.Success -> {
                    sharedFlow.tryEmit(MainViewModelState.ImagesLoaded(result.data))
                }

                UseCaseResult.Completed, UseCaseResult.Error -> sharedFlow.tryEmit(
                    MainViewModelState.Error
                )
            }
        }
    }
}