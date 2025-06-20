package com.app.filmfeed.presentation.screen.movie

import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.app.filmfeed.presentation.MovieViewModel

@Composable
fun WatchScreen(
    movieId: Long,
    isMovie: Boolean,
    viewModel: MovieViewModel
){
    val context = LocalContext.current
    val movies by viewModel.movies.collectAsState()
    val movie = movies.find { it.id == movieId }!!

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(if(isMovie) MediaItem.fromUri(movie.movieURL) else MediaItem.fromUri(movie.trailerURL))
            seekTo(viewModel.position)
            playWhenReady = viewModel.isPlaying
            prepare()
        }
    }

    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ){
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    useController = true // Встроенные элементы управления
                }
            },
            modifier = Modifier.fillMaxSize().aspectRatio(16f / 9f)
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.position = exoPlayer.currentPosition
            viewModel.isPlaying = exoPlayer.isPlaying
            exoPlayer.release()
        }
    }
}