package com.app.filmfeed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.filmfeed.Route
import com.app.filmfeed.getMovs
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard

@Composable
fun MainScreen(
    viewModel: MovieViewModel,
    paddingValues: PaddingValues,
    navController: NavController
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ){
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(getMovs()){
                    MovieCard(it){ navController.navigate(Route.AboutMovie.createRoute(it.id)) }
                }
            }
        }
    }
}