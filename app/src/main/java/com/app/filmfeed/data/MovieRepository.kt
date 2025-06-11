package com.app.filmfeed.data

interface MovieRepo {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovie(id: Int): Movie
    suspend fun getMember(id: Long): Member
}

class MovieRepository(private val apiService: ApiService): MovieRepo {
    override suspend fun getMovie(id: Int): Movie { return apiService.getMovie(id) }
    override suspend fun getMovies(): List<Movie> { return apiService.getMovies() }
    override suspend fun getMember(id: Long): Member { return apiService.getMember(id) }
}