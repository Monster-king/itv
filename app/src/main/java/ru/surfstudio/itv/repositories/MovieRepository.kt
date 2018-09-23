package ru.surfstudio.itv.repositories

import retrofit2.Response
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.*
import ru.surfstudio.itv.network.pojo.BaseResponse
import java.net.UnknownHostException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val service: MovieService) {

    private val movies: MutableMap<Int, List<Movie>> = HashMap()
    private var searchKey = "" // last searched query
    private val searchedMovies: MutableMap<Int, List<Movie>> = HashMap()

    fun getMovies(page: Int = 1): MovieRepoResult {
        if (page !in movies) {
            val result: Response<BaseResponse<Movie>>
            try {
                result = service.discoverMovies(page).execute()
            } catch (e: UnknownHostException) {
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

    fun searchMovies(query: String, page: Int = 1): MovieRepoResult {
        if (query != searchKey) {
            searchedMovies.clear()
        }
        if (page !in searchedMovies) {
            val result: Response<BaseResponse<Movie>>
            try {
                result = service.searchMovies(query, page).execute()
            } catch (e: UnknownHostException) {
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