package ru.surfstudio.itv.data.cache

import ru.surfstudio.itv.data.model.Movie


interface MovieCache {

    fun saveMovie(movie: Movie)

    fun removeMovie(movie: Movie)

    fun isSavedMovie(movie: Movie): Boolean

}