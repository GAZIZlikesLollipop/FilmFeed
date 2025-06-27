package com.app.filmfeed.data.repository

import android.content.Context
import com.app.filmfeed.DownloadedMovie
import com.app.filmfeed.UserData
import com.app.filmfeed.UserMovie
import com.app.filmfeed.data.userDataStore
import kotlinx.coroutines.flow.Flow

interface UserDataRepo {
    val userData: Flow<UserData>
    suspend fun updateWatchLaterMovies(data: List<Long>)
    suspend fun updateWatchedMovies(data: Map<Long,UserMovie>)
    suspend fun updateDownloadedMovies(data: Map<Long, DownloadedMovie>)
    suspend fun updateFavoriteMovies(data: List<Long>)
    suspend fun updateContinueWatchedMovies(data: Map<Long,UserMovie>)

    suspend fun deleteWatchedMovie(movieId: Long)
    suspend fun deleteDownloadedMovie(movieId: Long)
    suspend fun deleteContinueWatchedMovie(movieId: Long)
}

class UserDataRepository(context: Context) : UserDataRepo {
    private val dataStore = context.userDataStore
    override val userData: Flow<UserData> = dataStore.data
    override suspend fun updateFavoriteMovies(data: List<Long>) {
        dataStore.updateData {
            it.toBuilder()
                .clearFavoriteMovies()
                .addAllFavoriteMovies(data)
                .build()
        }
    }
    override suspend fun updateDownloadedMovies(data: Map<Long, DownloadedMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllDownloadedMovies(data)
                .build()
        }
    }
    override suspend fun updateWatchedMovies(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllWatchedMovies(data)
                .build()
        }
    }
    override suspend fun updateWatchLaterMovies(data: List<Long>) {
        dataStore.updateData {
            it.toBuilder()
                .clearWatchLaterMovies()
                .addAllWatchLaterMovies(data)
                .build()
        }
    }

    override suspend fun updateContinueWatchedMovies(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllContinueWatchMovies(data)
                .build()
        }
    }

    override suspend fun deleteDownloadedMovie(movieId: Long) {
        dataStore.updateData {
            it.toBuilder()
                .removeDownloadedMovies(movieId)
                .build()
        }
    }

    override suspend fun deleteWatchedMovie(movieId: Long) {
        dataStore.updateData {
            it.toBuilder()
                .removeWatchedMovies(movieId)
                .build()
        }
    }

    override suspend fun deleteContinueWatchedMovie(movieId: Long) {
        dataStore.updateData {
            it.toBuilder()
                .removeContinueWatchMovies(movieId)
                .build()
        }
    }
}