package com.app.filmfeed.presentation.screen.movie

import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.app.filmfeed.getMovs
import com.app.filmfeed.presentation.MovieViewModel
import kotlinx.coroutines.delay

@Composable
fun WatchMovieScreen(
    id: Long,
    viewModel: MovieViewModel
){
    val context = LocalContext.current
    val movie = getMovs()[id.toInt()]
    val config = LocalConfiguration.current
    val orientation = config.orientation

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(movie.movieURL))
            prepare()
            playWhenReady = true
        }
    }


    LaunchedEffect(exoPlayer) {
        while (true) {
//            Log.d("Info","${viewModel.duration}")
//            if(viewModel.duration >= exoPlayer.currentPosition){
                viewModel.duration = exoPlayer.currentPosition
                delay(100)
//            } else delay(100)
        }
    }

    LaunchedEffect(orientation) {
        while (true) {
            if(exoPlayer.duration <= 0){
                delay(100)
            }else{
                if(viewModel.duration > 0){
                    exoPlayer.seekTo(viewModel.duration)
                    break
                }else{
                    break
                }
            }
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
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16f / 9f)
        )
    }
    DisposableEffect(Unit) { onDispose { exoPlayer.release() } }
}