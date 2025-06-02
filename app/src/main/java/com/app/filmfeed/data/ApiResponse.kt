package com.app.filmfeed.data

enum class MovieCategories {
    Drama,
    Fantasy,
    Action,
    Comedy,
    Crime,
    Documentary,
    History,
    Horror,
    Musical,
    Mystery,
    Romance,
    ScienceFiction,
    Thriller,
    Sport,
    Adventure,
    Biographical,
    Animation
}

data class MovieItem(
    val id: Long,
    val name: String,
    val promoPoster: String,
    val categories: List<MovieCategories>,
    val rating: Double,
    val userRating: Double,
    val reviews: Long,
    val isWatched: Boolean,
    val description: String,
    val country: String,
    val createdYear: Int,
    val members: List<MovieMember>,
    val views: Long
)

data class MovieMember(
    val id: Long,
    val name: String,
    val featuredFilms: List<MovieItem>,
    val photo: String,
    val role: String,
    val age: Int
)