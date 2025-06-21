package com.app.filmfeed.data.repository

import android.content.Context
import com.app.filmfeed.UserData
import com.app.filmfeed.UserMovie
import com.app.filmfeed.data.userDataStore
import kotlinx.coroutines.flow.Flow

interface UserDataRepo {
    val userData: Flow<UserData>
    suspend fun updateWatchLaterMovie(data: Map<Long,UserMovie>)
    suspend fun updateWatchedMovie(data: Map<Long,UserMovie>)
    suspend fun updateDownloadedMovie(data: Map<Long,UserMovie>)
    suspend fun updateFavoriteMovie(data: Map<Long,UserMovie>)

    suspend fun deleteWatchLaterMovie(movieId: Long)
    suspend fun deleteWatchedMovie(movieId: Long)
    suspend fun deleteDownloadedMovie(movieId: Long)
    suspend fun deleteFavoriteMovie(movieId: Long)
}

class UserDataRepository(context: Context) : UserDataRepo {
    private val dataStore = context.userDataStore
    override val userData: Flow<UserData> = dataStore.data
    override suspend fun updateFavoriteMovie(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllFavoriteMovies(data)
                .build()
        }
    }
    override suspend fun updateDownloadedMovie(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllDownloadedMovies(data)
                .build()
        }
    }
    override suspend fun updateWatchedMovie(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllWatchedMovies(data)
                .build()
        }
    }
    override suspend fun updateWatchLaterMovie(data: Map<Long, UserMovie>) {
        dataStore.updateData {
            it.toBuilder()
                .putAllWatchLaterMovies(data)
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

    override suspend fun deleteFavoriteMovie(movieId: Long) {
        dataStore.updateData {
            it.toBuilder()
                .removeFavoriteMovies(movieId)
                .build()
        }
    }

    override suspend fun deleteWatchLaterMovie(movieId: Long) {
        dataStore.updateData {
            it.toBuilder()
                .removeWatchLaterMovies(movieId)
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
}