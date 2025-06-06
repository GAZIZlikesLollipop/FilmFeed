package com.app.filmfeed.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MovieViewModel(): ViewModel() {
    var duration by mutableLongStateOf(0L)
}