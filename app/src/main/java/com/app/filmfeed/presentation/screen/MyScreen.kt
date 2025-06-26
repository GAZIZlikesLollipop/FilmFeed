package com.app.filmfeed.presentation.screen

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard
import com.app.filmfeed.presentation.components.TwoColumnTextRow
import java.io.File

@Composable
fun MyScreen(
    viewModel: MovieViewModel,
    navController: NavController,
    paddingValues: PaddingValues
){
    val watchedMovies by viewModel.watchedMovies.collectAsState()
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val watchLater by viewModel.watchLater.collectAsState()
    val downloadedMovies by viewModel.downloadedMovies.collectAsState()
    val downloadState by viewModel.downloadState.collectAsState()
    val downloadStatus by viewModel.downloadStatus.collectAsState()
    val categories = stringArrayResource(R.array.my_categories)
    val movies by viewModel.movies.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ){
        items(categories){ category ->
            when {
                category == categories[0] && watchLater.isNotEmpty() -> {
                    TwoColumnTextRow(
                        firstText = category,
                        secondText = stringArrayResource(R.array.main_cnt)[0],
                    ) {}
                }
                category == categories[1] && downloadedMovies.isNotEmpty() -> {
                    TwoColumnTextRow(
                        firstText = category,
                        secondText = stringArrayResource(R.array.main_cnt)[0],
                    ) {}
                }
                category == categories[2] && favoriteMovies.isNotEmpty() -> {
                    TwoColumnTextRow(
                        firstText = category,
                        secondText = stringArrayResource(R.array.main_cnt)[0],
                    ) {}
                }
                category == categories[3] && watchedMovies.isNotEmpty() -> {
                    TwoColumnTextRow(
                        firstText = category,
                        secondText = stringArrayResource(R.array.main_cnt)[0],
                    ) {}
                }
                else -> {}
            }
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(
                    when (category) {
                        categories[0] -> watchLater
                        categories[1] -> downloadedMovies.keys.toList()
                        categories[2] -> favoriteMovies
                        else -> watchedMovies.keys.toList()
                    }
                ) { ind ->
                    movies.find { it.id == ind }?.let {
                        MovieCard(
                            movie = it
                        ) {
                            if(category == categories[1]){
                                val dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                                viewModel.mediaItem = MediaItem.fromUri(File(dir,"${it.name}.mp4".replace(" ","").lowercase()).toUri())
                                navController.navigate(Route.Watch.createRoute(it.id))
                            }else{
                                navController.navigate(Route.AboutMovie.createRoute(it.id))
                            }
                        }
                    }
                }
            }
        }
    }
}