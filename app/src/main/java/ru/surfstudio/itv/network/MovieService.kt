package ru.surfstudio.itv.network

import retrofit2.Call
import retrofit2.http.GET
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.pojo.BaseResponse

interface MovieService {

    @GET("discover/movie?page={page}")
    fun discoverMovies(page: Int = 1): Call<BaseResponse<Movie>>

}