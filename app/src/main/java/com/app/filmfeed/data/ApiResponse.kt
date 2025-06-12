package com.app.filmfeed.data

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Long,
    val name: String
)

@Serializable
data class Movie(
    val id: Long,
    val name: String,
    val posterURL: String,
    val movieURL: String,
    val trailerURL: String? = null,
    val duration: Int,
    val age: Int,
    val genres: List<Genre>,
    val rating: Double,
    val reviews: Long,
    val description: String,
    val country: String,
    val year: Int,
    val budget: Long,
    val boxOffice: Long,
    val movieMembers: List<MovieMember>,
//    val createdAt: OffsetTime,
//    val updatedAt: OffsetTime
)

@Serializable
data class MovieMember(
    val movieId: Long,
    val memberId: Long,
    val character: String?,
    val roles: List<String>,
    val member: Member
)

@Serializable
data class Member(
    val id: Long,
    val name: String,
    val photo: String,
    val roles: List<String>,
    val birthDate: String,
    val deathDate: String? = null,
    val biography: String,
    val nationality: String,
    val featuredFilms: List<Movie> = emptyList(),
//    val createdAt: OffsetTime,
//    val updatedAt: OffsetTime
)

data class User(
    val id: Long,
    val name: String,
    val avatarURL: String,
    val watchedMovies: Map<Movie, UserMovies>,
    val downloadMovies: Map<Movie, UserMovies>
)

data class UserMovies(
    val rating: Double,
    val isWatched: Boolean,
    val isInMy: Boolean,
    val durProgress: Long,
    val watchedDate: String
)