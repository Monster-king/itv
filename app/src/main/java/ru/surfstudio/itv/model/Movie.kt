package ru.surfstudio.itv.model

import com.google.gson.annotations.SerializedName

data class Movie(
        val title: String,
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("release_date")
        val releaseDate: String
)