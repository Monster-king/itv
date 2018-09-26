package ru.surfstudio.itv.repositories

import android.util.Log
import retrofit2.Response
import ru.surfstudio.itv.data.cache.MovieCache
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.*
import ru.surfstudio.itv.network.pojo.BaseResponse
import ru.surfstudio.itv.utils.Constants
import ru.surfstudio.itv.utils.toInt
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val service: MovieService,
                                          private val cache: MovieCache) {
    init {
        Log.i("created", "Repository")
    }

    private val movies: MutableList<Movie> = ArrayList()
    private var searchQuery = "" // last searched query
    private val searchedMovies: MutableList<Movie> = ArrayList()

    @Synchronized
    fun refresh() {
        movies.clear()
        searchedMovies.clear()
    }

    @Synchronized
    fun getMovies(startPosition: Int, loadSize: Int, searchQuery: String): MovieRepoResult {
        val start: Int
        if (searchQuery != this.searchQuery) {
            searchedMovies.clear()
            this.searchQuery = searchQuery
            start = 0
        } else {
            start = startPosition
        }
        return if (searchQuery == "")
            loadData(start, loadSize, movies) {
                service.discoverMovies(calculatePage(movies.size)).execute()
            }
        else {
            loadData(start, loadSize, searchedMovies) {
                service.searchMovies(searchQuery, calculatePage(searchedMovies.size)).execute()
            }
        }
    }

    private fun loadData(startPosition: Int,
                         loadSize: Int,
                         dataContainer: MutableList<Movie>,
                         loadSource: () -> Response<BaseResponse<Movie>>): MovieRepoResult {
        if (startPosition >= dataContainer.size) {
            val result: Response<BaseResponse<Movie>>
            try {
                result = loadSource.invoke()
            } catch (e: IOException) {
                return NoInternet
            }
            if (result.isSuccessful) {
                result.body()?.results?.forEach {
                    it.isFavourite = cache.isSavedMovie(it)
                }
                dataContainer.addAll(result.body()?.results ?: emptyList())
            } else {
                return Error(result.message()) // todo first we must try get error message from result.errorBody()
            }
        }
        return if (startPosition > dataContainer.size) {
            if (dataContainer.size <= loadSize)
                Success(ArrayList(dataContainer.subList(0, dataContainer.size)), 0)
            else {
                Success(ArrayList(dataContainer.subList(dataContainer.size - loadSize, loadSize)),
                        dataContainer.size - loadSize)
            }
        } else if (startPosition == dataContainer.size && startPosition % Constants.LOAD_SIZE != 0) {
            Success(emptyList(), startPosition)
        } else if (startPosition + loadSize >= dataContainer.size) {
            Success(ArrayList(dataContainer.subList(startPosition, dataContainer.size)), startPosition)
        } else {
            Success(ArrayList(dataContainer.subList(startPosition, startPosition + loadSize)), startPosition)
        }
    }

    private fun calculatePage(containerSize: Int): Int {
        return (containerSize / Constants.LOAD_SIZE) + (containerSize % Constants.LOAD_SIZE > 0).toInt() + 1
    }
}