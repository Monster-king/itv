package ru.surfstudio.itv.data.cache

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.utils.Constants
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import com.google.gson.reflect.TypeToken

// todo in real project prefer to use database to caching data
@Singleton
class SharedPrefCache @Inject constructor(
        @param:Named(Constants.APP_CONTEXT) private val context: Context
) : MovieCache {

    companion object {
        const val PREFS_NAME = "itv"
        const val MOVIES = "movies"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    private val savedMovies: MutableSet<Int> = HashSet()
    private val gson = Gson()

    init {
        val type = object : TypeToken<HashSet<Int>>() {}.type
        savedMovies.addAll(gson.fromJson(prefs.getString(MOVIES, "[]"), type))
    }

    @Synchronized
    override fun saveMovie(movie: Movie) {
        savedMovies.add(movie.id)
        prefs.edit().putString(MOVIES, gson.toJson(savedMovies)).apply()
    }

    @Synchronized
    override fun removeMovie(movie: Movie) {
        savedMovies.remove(movie.id)
        prefs.edit().putString(MOVIES, gson.toJson(savedMovies)).apply()
    }

    @Synchronized
    override fun isSavedMovie(movie: Movie): Boolean {
        return movie.id in savedMovies
    }

}