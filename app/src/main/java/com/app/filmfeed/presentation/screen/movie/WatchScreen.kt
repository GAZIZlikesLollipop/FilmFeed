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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.app.filmfeed.UserMovie
import com.app.filmfeed.presentation.MovieViewModel

@Composable
fun WatchScreen(
    viewModel: MovieViewModel,
    movieId: Long
){
    val context = LocalContext.current
    val continueWatching by viewModel.continueWatching.collectAsState()
    val downloadedMovies by viewModel.downloadedMovies.collectAsState()

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            viewModel.mediaItem?.let { setMediaItem(it) }
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
            if(downloadedMovies.containsKey(movieId)){
                val map = downloadedMovies.toMutableMap()
                val userMovie = map[movieId]?.toBuilder()
                    ?.setDurationProgress(exoPlayer.currentPosition)
                    ?.build() ?: UserMovie.newBuilder().setDurationProgress(exoPlayer.currentPosition).build()
                map.put(movieId,userMovie)
                viewModel.updateDownloaded(map)
            }else {
                val map = continueWatching.toMutableMap()
                val userMovie = map[movieId]?.toBuilder()
                    ?.setDurationProgress(exoPlayer.currentPosition)
                    ?.build() ?: UserMovie.newBuilder().setDurationProgress(exoPlayer.currentPosition).build()
                map.put(movieId,userMovie)
                viewModel.updateContinueWatching(map)
            }
            viewModel.position = exoPlayer.currentPosition
            viewModel.isPlaying = exoPlayer.isPlaying
            exoPlayer.release()
        }
    }
}