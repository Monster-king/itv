package ru.surfstudio.itv.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.surfstudio.itv.R
import java.lang.IllegalArgumentException

object ViewHolderFactory {

    const val MOVIE = 1
    const val NETWORK_STATE = 2

    fun create(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (type) {
            MOVIE -> {
                MovieViewHolder(layoutInflater.inflate(R.layout.item_card_cinema, parent, false))
            }
            NETWORK_STATE -> {
                NetworkStateViewHolder(layoutInflater.inflate(R.layout.item_network_state, parent, false))
            }
            else -> {
                throw IllegalArgumentException("Unknown view type: $type")
            }
        }
    }
}