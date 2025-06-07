package com.app.filmfeed.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem

class MovieViewModel(): ViewModel() {
    var position by mutableLongStateOf(0L)
    var isPlaying by mutableStateOf(true)
    var mediaItem by mutableStateOf<MediaItem?>(null)
}