package ru.surfstudio.itv.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_network_state.view.*
import ru.surfstudio.itv.network.Failed
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState

class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(networkState: NetworkState) {
        when(networkState) {
            Loading -> {
                updateViews(View.VISIBLE, View.GONE)
            }
            is Failed -> {
                updateViews(View.GONE, View.VISIBLE)
            }
        }
    }

    private fun updateViews(progress: Int, retry: Int) {
        with(itemView) {
            progress_bar.visibility = progress
            retry_btn.visibility = retry
        }
    }

}