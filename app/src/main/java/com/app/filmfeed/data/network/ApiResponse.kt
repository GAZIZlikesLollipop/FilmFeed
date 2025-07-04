package com.app.filmfeed.data.network

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
    val trailerURL: String,
    val duration: Long,
    val age: Int,
    val genres: List<Genre> = emptyList(),
    val rating: Double,
    val reviews: Long,
    val description: String,
    val country: String,
    val year: Int,
    val budget: Long,
    val boxOffice: Long,
    val movieMembers: List<MovieMember> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = ""
)

@Serializable
data class MovieMember(
    val movieId: Long,
    val memberId: Long,
    val character: String? = null,
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
    val featuredFilms: List<Movie>,
    val createdAt: String = "",
    val updatedAt: String = ""
)