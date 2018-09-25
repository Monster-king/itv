package ru.surfstudio.itv.data.model

import com.google.gson.annotations.SerializedName
import android.support.v7.util.DiffUtil


data class Movie(
        val id: Int,
        val title: String,
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("release_date")
        val releaseDate: String,
        @Transient
        var isFavourite: Boolean = false
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.overview == newItem.overview &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.releaseDate == newItem.releaseDate
            }
        }
    }

}