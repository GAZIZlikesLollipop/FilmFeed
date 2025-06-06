package com.app.filmfeed.data

enum class MovieCategories {
    Action, // Драма
    Mystery, // Мистика
    Romance, // Романтика
    Sport, // Спорт
    Biographical, // Биографический
    Family, // Семейный
    War, // Война
    Western, // Октябрьская
    Biography, // Биография
    Animation, // Анимация
    Musical, // Музыкальный
    Documentary, // Документальный
    History, // Исторический
    Fantasy, // Фэнтези
    Horror, // Ужасы
    ScienceFiction, // Научная фантастика
    Comedy, // Комедия
    Crime, // Преступление
    Thriller, // Триллер
    Drama, // Драма
    Adventure,
    MeloDrama
}

data class MovieItem(
    val id: Long,
    val name: String,
    val posterURL: String,
    val movieURL: String,
    val duration: Int,
    val age: Int,
    val categories: List<MovieCategories>,
    val rating: Double,
    val reviews: Long,
    val description: String,
    val country: String,
    val year: Int,
    val members: List<MovieMember>,
)

data class MovieMember(
    val id: Long,
    val name: String,
    val featuredFilms: List<MovieItem>,
    val photo: String,
    val role: String,
    val date: String
)

data class User(
    val id: Long,
    val name: String,
    val watchedMovies: Map<MovieItem, UserMovies>
)

data class UserMovies(
    val rating: Double,
    val isWatched: Boolean,
    val isInMy: Boolean,
    val durProgress: Long
)