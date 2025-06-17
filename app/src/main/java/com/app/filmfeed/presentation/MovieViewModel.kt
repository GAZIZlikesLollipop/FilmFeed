package com.app.filmfeed.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.filmfeed.data.Movie
import com.app.filmfeed.data.MovieRepository
import com.app.filmfeed.utils.Filters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ApiState {
    object Loading: ApiState
    data class Error(val message: String): ApiState
    data class Success(val movies: List<Movie>): ApiState
}

class MovieViewModel(private val movieRepository: MovieRepository): ViewModel() {
    var position by mutableLongStateOf(0L)
    var isPlaying by mutableStateOf(true)
    var isActor by mutableStateOf(true)

    private val _apiState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val apiState = _apiState.asStateFlow()

    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies = _movies.asStateFlow()

    var currentMovieId by mutableLongStateOf(0)

    var isRefreshing by mutableStateOf(false)

    var searchText by mutableStateOf("")
    var showFilterSheet by mutableStateOf(false)

    var filters by mutableStateOf(Filters())

    fun getMovies(){
        _apiState.value = ApiState.Loading
        viewModelScope.launch {
            _apiState.value = try {
                val response = movieRepository.getMovies()
                _movies.value = response
                ApiState.Success(response)
            }catch (e: Exception){
                ApiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}