package com.app.filmfeed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.app.filmfeed.R
import com.app.filmfeed.presentation.MovieViewModel

@Composable
fun SearchScreen(
    viewModel: MovieViewModel,
    paddingValues: PaddingValues
){
    val cnt = stringArrayResource(R.array.search_cnt)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ){

    }
}