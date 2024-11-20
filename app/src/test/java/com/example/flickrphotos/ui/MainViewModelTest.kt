package com.example.flickrphotos.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.flickrphotos.domain.results.UseCaseResult
import com.example.flickrphotos.domain.usecase.FlickrUseCase
import com.example.flickrphotos.ui.model.FlickrPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var flickrUseCase: FlickrUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        flickrUseCase = mock()
        viewModel = MainViewModel(flickrUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Init`() = runTest {
        val states = mutableListOf<MainViewModelState>()
        val job = launch {
            viewModel.getSharedFlow().toList(states)
        }

        // No intent sent, so state should remain Init
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(states.isEmpty())

        job.cancel()
    }

    @Test
    fun `loading images transitions through loading and success states`() = runTest {
        val mockPhotos = listOf(
            FlickrPhoto("1", "title1", "url1", "date", "url"),
            FlickrPhoto("2", "title2", "url2", "date", "url")
        )

        whenever(flickrUseCase.getFlickrPhotos("test query"))
            .thenReturn(UseCaseResult.Success(mockPhotos))

        val states = mutableListOf<MainViewModelState>()
        val job = launch {
            viewModel.getSharedFlow().toList(states)
        }

        viewModel.sendIntent(MainViewModelIntent.LoadImagesFromQuery("test query"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, states.size)
        assertTrue(states[0] is MainViewModelState.Loading)
        assertTrue(states[1] is MainViewModelState.ImagesLoaded)
        assertEquals(mockPhotos, (states[1] as MainViewModelState.ImagesLoaded).images)

        job.cancel()
    }

    @Test
    fun `loading images with error returns error state`() = runTest {
        whenever(flickrUseCase.getFlickrPhotos("error query"))
            .thenReturn(UseCaseResult.Error)

        val states = mutableListOf<MainViewModelState>()
        val job = launch {
            viewModel.getSharedFlow().toList(states)
        }

        viewModel.sendIntent(MainViewModelIntent.LoadImagesFromQuery("error query"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, states.size)
        assertTrue(states[0] is MainViewModelState.Loading)
        assertTrue(states[1] is MainViewModelState.Error)

        job.cancel()
    }

    @Test
    fun `loading multiple times updates state correctly`() = runTest {
        val successPhotos1 = listOf(FlickrPhoto("1", "title1", "url1", "date", "url"))
        val successPhotos2 = listOf(FlickrPhoto("2", "title2", "url2", "date", "url"))

        whenever(flickrUseCase.getFlickrPhotos("query1"))
            .thenReturn(UseCaseResult.Success(successPhotos1))
        whenever(flickrUseCase.getFlickrPhotos("query2"))
            .thenReturn(UseCaseResult.Success(successPhotos2))

        val states = mutableListOf<MainViewModelState>()
        val job = launch {
            viewModel.getSharedFlow().toList(states)
        }

        viewModel.sendIntent(MainViewModelIntent.LoadImagesFromQuery("query1"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.sendIntent(MainViewModelIntent.LoadImagesFromQuery("query2"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(4, states.size)
        assertTrue(states[0] is MainViewModelState.Loading)
        assertTrue(states[1] is MainViewModelState.ImagesLoaded)
        assertTrue(states[2] is MainViewModelState.Loading)
        assertTrue(states[3] is MainViewModelState.ImagesLoaded)

        job.cancel()
    }
}