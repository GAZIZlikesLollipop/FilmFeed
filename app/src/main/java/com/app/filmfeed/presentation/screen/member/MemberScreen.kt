package com.app.filmfeed.presentation.screen.member

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.app.filmfeed.presentation.MovieViewModel

@Composable
fun MemberScreen(
    id: Long,
    paddingValues: PaddingValues,
    viewModel: MovieViewModel
){

    val member by viewModel.member.collectAsState()

    LaunchedEffect(Unit) {
        if(member == null){
            viewModel.getMember(id)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {

    }
}