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
    val trailer: String? = null,
    val duration: Int,
    val age: Int,
    val categories: List<MovieCategories>,
    val rating: Double,
    val reviews: Long,
    val description: String,
    val country: String,
    val year: Int,
    val members: List<MovieMember>,
    val tags: List<String> = emptyList()
)

data class MovieMember(
    val id: Long,
    val name: String,
    val featuredFilms: List<MovieItem>,
    val photo: String,
    val roles: List<String>,
    val character: String? = null,
    val birthDate: String
)

data class User(
    val id: Long,
    val name: String,
    val avatarURL: String,
    val watchedMovies: Map<MovieItem, UserMovies>,
    val downloadMovies: Map<MovieItem, UserMovies>
)

data class UserMovies(
    val rating: Double,
    val isWatched: Boolean,
    val isInMy: Boolean,
    val durProgress: Long,
    val watchedDate: String
)