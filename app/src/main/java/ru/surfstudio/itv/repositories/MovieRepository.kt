package ru.surfstudio.itv.repositories

import retrofit2.Response
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.*
import ru.surfstudio.itv.network.pojo.BaseResponse
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val service: MovieService) {

    private val movies: ConcurrentHashMap<Int, List<Movie>> = ConcurrentHashMap()
    private var searchKey = "" // last searched query
    private val searchedMovies: MutableMap<Int, List<Movie>> = HashMap()

    @Synchronized fun refresh() {
        movies.clear()
        searchedMovies.clear()
    }

    @Synchronized fun getMovies(page: Int = 1): MovieRepoResult {
        if (page !in movies) {
            val result: Response<BaseResponse<Movie>>
            try {
                result = service.discoverMovies(page).execute()
            } catch (e: IOException ) {
                return NoInternet
            }
            if (result.isSuccessful) {
                movies[page] = result.body()?.results ?: emptyList()
            } else {
                return Error(result.message()) // todo first we must try get error message from result.errorBody()
            }
        }
        return Success(movies[page] ?: emptyList())
    }

    @Synchronized fun searchMovies(query: String, page: Int = 1): MovieRepoResult {
        if (query != searchKey) {
            searchedMovies.clear()
        }
        if (page !in searchedMovies) {
            val result: Response<BaseResponse<Movie>>
            try {
                result = service.searchMovies(query, page).execute()
            } catch (e: IOException) {
                return NoInternet
            }
            if (result.isSuccessful) {
                searchedMovies[page] = result.body()?.results ?: emptyList()
            } else {
                return Error(result.message()) // todo first we must try get error message from result.errorBody()
            }
        }
        return Success(searchedMovies[page] ?: emptyList())
    }

}