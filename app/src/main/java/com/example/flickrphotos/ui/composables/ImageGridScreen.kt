package com.example.flickrphotos.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.flickrphotos.ui.MainViewModel
import com.example.flickrphotos.ui.MainViewModelIntent
import com.example.flickrphotos.ui.MainViewModelState
import com.example.flickrphotos.ui.model.FlickrPhoto

@Composable
fun ImageGridScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val state = viewModel.getSharedFlow().collectAsState(initial = MainViewModelState.Init)
        var searchQuery by remember { mutableStateOf("") }

        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                        viewModel.sendIntent(MainViewModelIntent.LoadImagesFromQuery(newQuery))
                    },
                    label = { Text("Search for images") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (val result = state.value) {
                    MainViewModelState.Error -> {
                    }

                    MainViewModelState.Loading -> {
                        LoadingScreen()
                    }

                    is MainViewModelState.ImagesLoaded -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(result.images) { image ->
                                ImageItem(image, navController)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ImageItem(image: FlickrPhoto, navController: NavHostController) {
    AsyncImage(model = image.image,
        contentDescription = image.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("image", image)
                navController.navigate("image_details")
            })
}