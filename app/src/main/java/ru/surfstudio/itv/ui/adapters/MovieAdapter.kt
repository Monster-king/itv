package ru.surfstudio.itv.ui.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.viewholders.MovieViewHolder
import ru.surfstudio.itv.ui.viewholders.NetworkStateViewHolder
import ru.surfstudio.itv.ui.viewholders.ViewHolderFactory




class MovieAdapter : PagedListAdapter<Movie, RecyclerView.ViewHolder>(Movie.DIFF_CALLBACK) {

    val retryClick: PublishSubject<Any> = PublishSubject.create()
    val favouriteClick: PublishSubject<Movie> = PublishSubject.create()

    var networkState: NetworkState? = null
    set(value) {
        val previousState = field
        val previousExtraRow = hasExtraRow()
        field = value
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== field) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            ViewHolderFactory.NETWORK_STATE
        } else {
            ViewHolderFactory.MOVIE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MovieViewHolder -> {
                holder.bindView(getItem(position)!!)
                holder.favouriteClick.subscribe {
                    favouriteClick.onNext(it)
                }
            }
            is NetworkStateViewHolder -> {
                holder.bindView(networkState ?: Loading)
                holder.retryClick.subscribe {
                    retryClick.onNext(it)
                }
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != Loaded
    }

}