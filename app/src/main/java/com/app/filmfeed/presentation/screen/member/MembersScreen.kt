package com.app.filmfeed.presentation.screen.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.DetailMemberCard

@Composable
fun MembersScreen(
    id: Long,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: MovieViewModel
){
    val movies by viewModel.movies.collectAsState()
    val movie = movies.find { it.id == id }!!
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(movie.movieMembers){
            if(if(viewModel.isActor) it.character != null else it.character == null) {
                DetailMemberCard(it, navController = navController)
            }
        }
    }
}