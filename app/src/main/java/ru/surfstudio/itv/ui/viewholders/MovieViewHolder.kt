package ru.surfstudio.itv.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.surfstudio.itv.R
import kotlinx.android.synthetic.main.item_card_cinema.*
import kotlinx.android.synthetic.main.item_card_cinema.view.*
import ru.surfstudio.itv.model.Movie

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(movie: Movie) {
        with(itemView) {
            title_tv.text = movie.title
            overview_tv.text = movie.overview
            date_tv.text = movie.releaseDate
            favourite_btn.isSelected = movie.isFavourite
            Glide.with(itemView).load(movie.posterPath).into(poster_image)
        }
    }

}