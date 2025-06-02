package com.app.filmfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.BaseScreen
import com.app.filmfeed.presentation.theme.FilmFeedTheme

class MainActivity : ComponentActivity() {
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = viewModels<MovieViewModel>().value
        enableEdgeToEdge()
        setContent {
            FilmFeedTheme {
                BaseScreen(movieViewModel)
            }
        }
    }
}