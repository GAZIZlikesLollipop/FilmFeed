package com.app.filmfeed.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.app.filmfeed.data.Member
import com.app.filmfeed.data.Movie
import com.app.filmfeed.data.MovieRepository
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
    var mediaItem by mutableStateOf<MediaItem?>(null)
    var isActor by mutableStateOf(true)

    private val _apiState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val apiState = _apiState.asStateFlow()

    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies = _movies.asStateFlow()

    private val _member = MutableStateFlow<Member?>(null)
    val member = _member.asStateFlow()
    
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
    
    fun getMember(id: Long){
        viewModelScope.launch { 
            try {
                _member.value = movieRepository.getMember(id)
            } catch (e: Exception){
                _member.value = Member(
                    id = 9081263456348517234L,
                    name = e.localizedMessage ?: "",
                    photo = "",
                    roles = emptyList(),
                    birthDate = "",
                    biography = "",
                    nationality = "",
                    featuredFilms = emptyList()
                )
            }
        }
    }

//    fun getMovie(id: Int){
//        _apiState.value = ApiState.Loading
//        viewModelScope.launch {
//            _apiState.value = try {
//                ApiState.Success(movie = movieRepository.getMovie(id))
//            }catch (e: Exception){
//                ApiState.Error(e.localizedMessage ?: "")
//            }
//        }
//    }

}