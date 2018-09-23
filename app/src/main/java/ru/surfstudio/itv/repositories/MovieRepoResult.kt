package ru.surfstudio.itv.repositories

import ru.surfstudio.itv.model.Movie

sealed class MovieRepoResult
data class Success(val movies: List<Movie>) : MovieRepoResult()
data class Error(val message: String) : MovieRepoResult()
object NoInternet: MovieRepoResult()