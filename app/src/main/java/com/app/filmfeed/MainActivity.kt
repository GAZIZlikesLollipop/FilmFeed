package com.app.filmfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.app.filmfeed.data.network.ApiService
import com.app.filmfeed.data.network.createHttpClient
import com.app.filmfeed.data.repository.MovieRepository
import com.app.filmfeed.data.repository.UserDataRepository
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.BaseScreen
import com.app.filmfeed.presentation.theme.FilmFeedTheme
import com.app.filmfeed.utils.MovieViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieRepository = MovieRepository(ApiService(createHttpClient()))
        val userDataRepository = UserDataRepository(this)
        movieViewModel = viewModels<MovieViewModel>{
            MovieViewModelFactory(movieRepository,userDataRepository)
        }.value
        enableEdgeToEdge()
        setContent {
            FilmFeedTheme {
                BaseScreen(movieViewModel)
            }
        }
    }
}