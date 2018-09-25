package ru.surfstudio.itv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.pojo.BaseResponse

interface MovieService {

    @GET("discover/movie")
    fun discoverMovies(@Query("page") page: Int = 1): Call<BaseResponse<Movie>>

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String,
                     @Query("page") page: Int = 1): Call<BaseResponse<Movie>>

}
