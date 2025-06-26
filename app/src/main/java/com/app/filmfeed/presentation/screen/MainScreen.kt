@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.filmfeed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.ApiState
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard
import com.app.filmfeed.presentation.components.TwoColumnTextRow

@Composable
fun MainScreen(
    viewModel: MovieViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val apiState by viewModel.apiState.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val cnt = stringArrayResource(R.array.main_cnt)

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
                    item {
                        TwoColumnTextRow(
                            firstText = cats[3],
                            secondText = cnt[0]
                        ) {

                        }
                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(movies) {
                                MovieCard(it) {
                                    navController.navigate(Route.AboutMovie.createRoute(it.id))
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