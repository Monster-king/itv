package ru.surfstudio.itv.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_card_cinema.view.*
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.utils.Constants
import ru.surfstudio.itv.utils.DateUtils

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var movie: Movie
    val favouriteClick: Observable<Movie>

    init {
        favouriteClick = RxView.clicks(itemView.favourite_btn).map {
            movie.isFavourite = !movie.isFavourite
            itemView.favourite_btn.isSelected = !itemView.favourite_btn.isSelected
            movie
        }
    }

    fun bindView(movie: Movie) {
        this.movie = movie
        with(itemView) {
            title_tv.text = movie.title
            overview_tv.text = movie.overview
            date_tv.text = DateUtils.formatDate(movie.releaseDate)
            favourite_btn.isSelected = movie.isFavourite
            Glide.with(itemView)
                    .load(Constants.IMAGE_URL + movie.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(poster_image)
            Log.i("imagePath", Constants.IMAGE_URL + movie.posterPath)
        }
    }

}