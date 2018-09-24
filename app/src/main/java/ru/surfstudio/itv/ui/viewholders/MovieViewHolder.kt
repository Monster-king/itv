package ru.surfstudio.itv.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_card_cinema.view.*
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.utils.Constants

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(movie: Movie) {
        with(itemView) {
            title_tv.text = movie.title
            overview_tv.text = movie.overview
            date_tv.text = movie.releaseDate
            favourite_btn.isSelected = movie.isFavourite
            Glide.with(itemView)
                    .load(Constants.IMAGE_URL + movie.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(poster_image)
            Log.i("imagePath", Constants.IMAGE_URL + movie.posterPath)
        }
    }

}