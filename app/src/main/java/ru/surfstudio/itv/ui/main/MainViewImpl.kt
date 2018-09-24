package ru.surfstudio.itv.ui.main

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.error_layout.view.*
import ru.surfstudio.itv.R
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.adapters.MovieAdapter
import ru.surfstudio.itv.ui.base.BaseView
import javax.inject.Inject

@ActivityScope
class MainViewImpl @Inject constructor(context: Context,
                                       private val adapter: MovieAdapter) : BaseView(context), MainView {

    override val retryClicks: PublishSubject<Any>

    override val retryOnError: Observable<Any>

    override val swipeRefresh: Observable<Any>

    override val view: View = this

    init {
        inflate(R.layout.activity_main)
        retryOnError = RxView.clicks(retry_on_error)
        retryClicks = adapter.retryClick

        swipeRefresh = RxSwipeRefreshLayout.refreshes(swipe_refresh)
        movies_rv.adapter = adapter
        movies_rv.layoutManager = GridLayoutManager(context, 1) // todo get from dimens
    }


    override fun setNetworkState(networkState: NetworkState) {
        adapter.networkState = networkState
    }

    override fun showData() {
        swipe_refresh.isRefreshing = false
        updateViews(VISIBLE, GONE, GONE)
    }

    override fun showInitialLoading() {
        updateViews(GONE, VISIBLE, GONE)
    }

    override fun showError() {
        updateViews(GONE, GONE, VISIBLE)
    }

    private fun updateViews(content: Int, progress: Int, error: Int) {
        swipe_refresh.visibility = content
        progress_bar.visibility = progress
        error_layout.visibility = error
    }

}