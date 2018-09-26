package ru.surfstudio.itv.repositories

import ru.surfstudio.itv.data.model.Movie

sealed class MovieRepoResult
data class Success(val movies: List<Movie>, val startPosition: Int) : MovieRepoResult()
data class Error(val message: String) : MovieRepoResult()
object NoInternet: MovieRepoResult()