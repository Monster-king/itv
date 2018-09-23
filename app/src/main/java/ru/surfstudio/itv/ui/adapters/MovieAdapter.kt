package ru.surfstudio.itv.ui.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.viewholders.MovieViewHolder
import ru.surfstudio.itv.ui.viewholders.NetworkStateViewHolder
import ru.surfstudio.itv.ui.viewholders.ViewHolderFactory


class MovieAdapter : PagedListAdapter<Movie, RecyclerView.ViewHolder>(Movie.DIFF_CALLBACK) {

    private var networkState: NetworkState? = null


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            ViewHolderFactory.NETWORK_STATE
        } else {
            ViewHolderFactory.MOVIE
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != Loaded
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MovieViewHolder -> {
                holder.bindView(getItem(position)!!)
            }
            is NetworkStateViewHolder -> {
                holder.bindView(networkState ?: Loading)
            }
        }
    }

}