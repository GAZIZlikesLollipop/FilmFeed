@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.filmfeed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.ApiState
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard
import com.app.filmfeed.presentation.components.TwoColumnTextRow
import java.time.LocalDate

@Composable
fun MainScreen(
    viewModel: MovieViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val apiState by viewModel.apiState.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val cnt = stringArrayResource(R.array.main_cnt)
    val continueWatching by viewModel.continueWatching.collectAsState()
    val watchLater by viewModel.watchLater.collectAsState()

    LaunchedEffect(Unit) {
        if (movies.isEmpty()) {
            viewModel.getMovies()
        }
    }

    when (apiState) {
        is ApiState.Success -> {
            val cats = stringArrayResource(R.array.main_screen_categories)
            PullToRefreshBox(
                isRefreshing = viewModel.isRefreshing,
                onRefresh = {
                    viewModel.getMovies()
                },
                modifier = Modifier.padding(paddingValues),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cats){ cat ->
                        if(cat == cats[0]) {
                            if(continueWatching.isNotEmpty()) {
                                TwoColumnTextRow(
                                    firstText = cat,
                                    secondText = cnt[0]
                                ) {
                                    viewModel.mvScreenMovies.clear()
                                    viewModel.mvScreenMovies.addAll(movies.filter { continueWatching.contains(it.id) })
                                    navController.navigate(Route.Movies.route)
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    items(
                                        continueWatching.toList()
                                    ) { (ind,usMov) ->
                                        val movie = movies.find { it.id == ind }!!
                                        Box(
                                            modifier = Modifier.height(250.dp).width(150.dp)
                                        ) {
                                            MovieCard(movie) {
                                                viewModel.position = usMov.durationProgress
                                                viewModel.mediaItem = MediaItem.fromUri(movie.movieURL)
                                                navController.navigate(Route.Watch.createRoute(ind))
                                            }
                                            Column(
                                                modifier = Modifier.align(Alignment.BottomCenter),
                                                horizontalAlignment = Alignment.End,
                                                verticalArrangement = Arrangement.spacedBy(24.dp)
                                            ) {
                                                Text(
                                                    text = "${(movie.duration / 1000) / 3600} h ${((movie.duration / 1000) % 3600) / 60} min",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                                    modifier = Modifier.offset(y = (-8).dp)
                                                )
                                                LinearProgressIndicator(
                                                    progress = { usMov.durationProgress.toFloat() / movie.duration },
                                                    modifier = Modifier.fillMaxWidth(),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            TwoColumnTextRow(
                                firstText = cat,
                                secondText = cnt[0]
                            ) {
                                viewModel.mvScreenMovies.clear()
                                viewModel.mvScreenMovies.addAll(
                                    when(cat) {
                                        cats[1] -> movies
                                        cats[2] -> movies.filter { it.year >= LocalDate.now().year - 1 }
                                        cats[3] -> movies.filter { watchLater.contains(it.id) }
                                        cats[4] -> movies.filter { it.year >= LocalDate.now().year - 1 && it.budget < it.boxOffice }
                                        else -> movies
                                    }
                                )
                                navController.navigate(Route.Movies.route)
                            }

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(
                                    when(cat){
                                        cats[1] -> movies
                                        cats[2] -> movies.filter { it.year >= LocalDate.now().year - 1 }
                                        cats[3] -> movies.filter { watchLater.contains(it.id) }
                                        cats[4] -> movies.filter { it.year >= LocalDate.now().year - 1 && it.budget < it.boxOffice }
                                        else -> movies
                                    }
                                ) { movie ->
                                    MovieCard(movie) {
                                        navController.navigate(Route.AboutMovie.createRoute(movie.id))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        is ApiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }
        is ApiState.Error -> {
            val errCnt = stringArrayResource(R.array.error)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    errCnt[0],
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {viewModel.getMovies()},
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        errCnt[1],
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        is ApiState.Initial -> {}
    }
}