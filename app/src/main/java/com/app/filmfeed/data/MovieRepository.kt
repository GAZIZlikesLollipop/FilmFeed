package com.app.filmfeed.data

interface MovieRepo {
    suspend fun getMovies(): List<Movie>
}

class MovieRepository(private val apiService: ApiService): MovieRepo {
    override suspend fun getMovies(): List<Movie> { return apiService.getMovies() }
}