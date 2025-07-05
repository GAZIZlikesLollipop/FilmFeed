package com.app.filmfeed.presentation

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.app.filmfeed.DownloadedMovie
import com.app.filmfeed.Route
import com.app.filmfeed.UserMovie
import com.app.filmfeed.data.network.Movie
import com.app.filmfeed.data.repository.MovieRepository
import com.app.filmfeed.data.repository.UserDataRepository
import com.app.filmfeed.utils.Filters
import com.google.protobuf.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant

sealed interface ApiState {
    object Initial: ApiState
    object Loading: ApiState
    object Error: ApiState
    object Success: ApiState
}

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val userDataRepository: UserDataRepository
): ViewModel() {
    var isPlaying by mutableStateOf(true)
    var isActor by mutableStateOf(true)
    var downloadId by mutableLongStateOf(0)
    var mediaItem: MediaItem? by mutableStateOf(null)
    var position by mutableLongStateOf(0)

    private val _apiState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val apiState = _apiState.asStateFlow()

    var mvScreenMovies = mutableStateListOf<Movie>()
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies = _movies.asStateFlow()

    var currentMovieId by mutableLongStateOf(0)

    var isRefreshing by mutableStateOf(false)

    var searchText by mutableStateOf("")
    var showFilterSheet by mutableStateOf(false)
    var deleteMod by mutableStateOf(false)

    var filters by mutableStateOf(Filters())
    var previousRoute by mutableStateOf(Route.Main.route)

    val userData = userDataRepository.userData

    val favoriteGenres = userData
        .map { it.favoriteGenresList }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList<String>()
        )

    val watchedMovies = userData.map { it.watchedMoviesMap }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyMap<Long,UserMovie>()
    )

    val watchLater = userData.map { it.watchLaterMoviesList }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val favoriteMovies = userData.map { it.favoriteMoviesList }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val downloadedMovies = userData.map { it.downloadedMoviesMap }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyMap<Long, DownloadedMovie>()
    )

    val continueWatching = userData.map { it.continueWatchMoviesMap }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyMap<Long,UserMovie>()
    )

    fun getMovies(){
        _apiState.value = ApiState.Loading
        viewModelScope.launch {
            _apiState.value = try {
                val response = movieRepository.getMovies()
                _movies.value = response
                ApiState.Success
            }catch (_: Exception){
                ApiState.Error
            }
        }
    }

    fun addToWatchLater(movieId: Long){
        val list = watchLater.value.toMutableList()
        list.add(movieId)
        viewModelScope.launch { userDataRepository.updateWatchLaterMovies(list.toList()) }
    }
    fun addToFavorite(movieId: Long){
        val list = favoriteMovies.value.toMutableList()
        list.add(movieId)
        val list2 = favoriteGenres.value.toMutableSet()
        movies.value.find { it.id == movieId }!!.genres.forEach {
            list2.add(it.name)
        }
        viewModelScope.launch {
            userDataRepository.updateFavoriteMovies(list.toList())
        }
    }
    fun updateContinueWatching(data: Map<Long, UserMovie>){
        viewModelScope.launch {
            userDataRepository.updateContinueWatchedMovies(data)
        }
    }
    fun updateDownloaded(data: Map<Long, DownloadedMovie>){
        viewModelScope.launch {
            userDataRepository.updateDownloadedMovies(data)
        }
    }
    fun addToWatched(movieId: Long){
        val map = watchedMovies.value.toMutableMap()
        val now = Instant.now()
        val userMovie = map[movieId]?.toBuilder()
            ?.setIsWatched(true)
            ?.setLastWatchedData(
                Timestamp.newBuilder()
                    .setSeconds(now.epochSecond)
                    .setNanos(now.nano)
                    .build()
            )
            ?.build()?: UserMovie.newBuilder()
                .setIsWatched(true)
                .setLastWatchedData(
                    Timestamp.newBuilder()
                        .setSeconds(now.epochSecond)
                        .setNanos(now.nano)
                        .build()
                ).build()
        map.put(movieId,userMovie)
        viewModelScope.launch {
            userDataRepository.updateWatchedMovies(map.toMap())
        }
    }
    fun getDownloadState(context: Context,movieId: Long){
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        var cursor: Cursor? = null
        try {
            cursor = downloadManager.query(query)
            if(cursor != null && cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (statusIndex != -1) { // Проверяем, что столбец существует
                    val status = cursor.getInt(statusIndex)

                    when (status) {
                        DownloadManager.STATUS_PENDING,DownloadManager.STATUS_RUNNING -> {
                            val map = downloadedMovies.value.toMutableMap()
                            val userMovie = map[movieId]?.toBuilder()?.setApiState(1)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
                            map.put(movieId,userMovie)
                            updateDownloaded(map.toMap())
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            val map = downloadedMovies.value.toMutableMap()
                            val userMovie = map[movieId]?.toBuilder()?.setApiState(1)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
                            map.put(movieId,userMovie)
                            updateDownloaded(map.toMap())
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            val map = downloadedMovies.value.toMutableMap()
                            val userMovie = map[movieId]?.toBuilder()?.setApiState(2)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
                            map.put(movieId,userMovie)
                            updateDownloaded(map.toMap())
                        }
                        DownloadManager.STATUS_FAILED -> {
                            val map = downloadedMovies.value.toMutableMap()
                            val userMovie = map[movieId]?.toBuilder()?.setApiState(3)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
                            map.put(movieId,userMovie)
                            updateDownloaded(map.toMap())
                        }
                    }
                }
            } else {
                val map = downloadedMovies.value.toMutableMap()
                val userMovie = map[movieId]?.toBuilder()?.setApiState(0)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
                map.put(movieId,userMovie)
                updateDownloaded(map.toMap())
            }
        } catch(e: Exception){
            val map = downloadedMovies.value.toMutableMap()
            val userMovie = map[movieId]?.toBuilder()?.setApiState(3)?.build() ?: DownloadedMovie.newBuilder().setApiState(1).build()
            map.put(movieId,userMovie)
            updateDownloaded(map.toMap())
            Log.e("STATE ERROR",e.localizedMessage ?: "")
        } finally {
            cursor?.close()
        }
    }
    fun downloadMovie(
        context: Context,
        movieId: Long
    ){
        val movie = movies.value.find { it.id == movieId }
        val map = downloadedMovies.value.toMutableMap()
        val posterFile = "${movie?.name}.${movie?.posterURL?.substringAfterLast(".")}".replace(" ","").lowercase()
        val downloadedMovie = DownloadedMovie.newBuilder()
            .setApiState(0)
            .setName(movie?.name)
            .setDuration(movie?.duration ?: 0)
            .setPosterPath(File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),posterFile).toUri().toString())
            .build()
        map.put(movieId, downloadedMovie)
        val fileName = "${movie?.name}.mp4".replace(" ","").lowercase()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val posterReq = DownloadManager.Request(movie?.posterURL?.toUri())
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        posterReq.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_PICTURES,posterFile)
        val movieReq = DownloadManager.Request(movie?.movieURL?.toUri())
            .setTitle(fileName)
            .setDescription("Downloading...")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        movieReq.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_MOVIES,fileName)
        downloadId = downloadManager.enqueue(movieReq)
        downloadManager.enqueue(posterReq)
        Toast.makeText(context, "Download is started...", Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            userDataRepository.updateDownloadedMovies(map.toMap())
        }
    }
    fun evaluateMovie(
        movieId: Long,
        rating: Double
    ){
        val map = watchedMovies.value.toMutableMap()
        val now = Instant.now()
        val userMovie = map[movieId]?.toBuilder()
            ?.setRating(rating)
            ?.setLastWatchedData(
                Timestamp.newBuilder()
                    .setSeconds(now.epochSecond)
                    .setNanos(now.nano)
                    .build()
            )
            ?.build()?: UserMovie.newBuilder()
                .setRating(rating)
                .setLastWatchedData(
                    Timestamp.newBuilder()
                        .setSeconds(now.epochSecond)
                        .setNanos(now.nano)
                        .build()
                ).build()
        map.put(movieId,userMovie)
        viewModelScope.launch { userDataRepository.updateWatchedMovies(map.toMap()) }
    }
    fun deleteWatchLater(movieId: Long){
        val list = watchLater.value.toMutableList()
        list.remove(movieId)
        viewModelScope.launch {
            userDataRepository.updateWatchLaterMovies(list.toList())
        }
    }
    fun deleteWatched(movieId: Long){
        val map = watchedMovies.value.toMutableMap()
        val userMovie = map[movieId]?.toBuilder()
            ?.setIsWatched(false)
            ?.build() ?: UserMovie.newBuilder().setIsWatched(false).build()
        map.put(movieId,userMovie)
        viewModelScope.launch {
            userDataRepository.updateWatchedMovies(map)
        }
    }
    fun deleteDownloaded(
        context: Context,
        movieId: Long
    ){
        val movie = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),"${movies.value.find { it.id == movieId }?.name}.mp4".replace(" ", "").lowercase())
        val poster = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),"${movies.value.find { it.id == movieId }?.name}.png".replace(" ", "").lowercase())
        movie.delete()
        poster.delete()
        viewModelScope.launch {
            userDataRepository.deleteDownloadedMovie(movieId)
        }
    }
    fun deleteFavorite(movieId: Long){
        val list = favoriteMovies.value.toMutableList()
        list.remove(movieId)
        val list2 = favoriteGenres.value.toMutableSet()
        movies.value.find { it.id == movieId }!!.genres.forEach {
            list2.remove(it.name)
        }
        viewModelScope.launch {
            userDataRepository.updateFavoriteMovies(list.toList())
        }
    }
    fun deleteGrade(movieId: Long){
        val map = watchedMovies.value.toMutableMap()
        val userMovie = map[movieId]?.toBuilder()?.setRating(0.0)?.build() ?: UserMovie.newBuilder().setRating(0.0).build()
        map.put(movieId,userMovie)
        viewModelScope.launch {
            userDataRepository.updateWatchedMovies(map.toMap())
        }
    }
}