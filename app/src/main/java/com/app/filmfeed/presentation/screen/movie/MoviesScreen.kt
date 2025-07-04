package com.app.filmfeed.presentation.screen.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard

@Composable
fun MoviesScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    paddingValues: PaddingValues
){
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ){
        viewModel.mvScreenMovies.forEach {
            MovieCard(it) {
                navController.navigate(Route.AboutMovie.createRoute(it.id))
            }
        }
    }
}