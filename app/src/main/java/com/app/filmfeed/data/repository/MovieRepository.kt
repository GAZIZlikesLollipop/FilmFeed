package com.app.filmfeed.data.repository

import com.app.filmfeed.data.network.ApiService
import com.app.filmfeed.data.network.Movie

interface MovieRepo {
    suspend fun getMovies(): List<Movie>
}

class MovieRepository(private val apiService: ApiService): MovieRepo {
    override suspend fun getMovies(): List<Movie> { return apiService.getMovies() }
}