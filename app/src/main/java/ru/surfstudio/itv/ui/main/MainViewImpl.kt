package ru.surfstudio.itv.ui.main

import android.animation.ObjectAnimator
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.error_layout.view.*
import ru.surfstudio.itv.R
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.adapters.MovieAdapter
import ru.surfstudio.itv.ui.base.BaseView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class MainViewImpl @Inject constructor(context: Context,
                                       private val adapter: MovieAdapter) : BaseView(context), MainView {

    override val view: View = this

    override val retryClicks: PublishSubject<Any>

    override val retryOnError: Observable<Any>

    override val swipeRefresh: Observable<Any>

    override val favouriteClick: PublishSubject<Movie>

    override val searchTextChanges: Observable<String>

    init {
        inflate(R.layout.activity_main)
        retryOnError = RxView.clicks(retry_on_error)
        retryClicks = adapter.retryClick
        favouriteClick = adapter.favouriteClick

        swipeRefresh = RxSwipeRefreshLayout.refreshes(swipe_refresh)
        searchTextChanges = RxTextView.textChanges(search_tv).map {
            it.toString()
        }.debounce(1, TimeUnit.SECONDS)

        movies_rv.adapter = adapter
        movies_rv.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.span_count)) // todo get from dimens
    }


    override fun setNetworkState(networkState: NetworkState) {
        adapter.networkState = networkState
    }

    override fun showData() {
        updateViews(VISIBLE, GONE, GONE)
    }

    override fun showError() {
        updateViews(GONE, GONE, VISIBLE)
    }

    override fun showSnackBar() {
        Snackbar.make(this, resources.getText(R.string.error_text), Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        if (adapter.itemCount > 0) {
            showLoadingHasData()
        } else {
            showInitialLoading()
        }
    }

    private fun updateViews(content: Int, initialProgress: Int, error: Int, progress: Int = GONE) {
        swipe_refresh.visibility = content
        initial_progress.visibility = initialProgress
        error_layout.visibility = error
        progress_bar.visibility = progress
    }

    private fun showInitialLoading() {
        updateViews(GONE, VISIBLE, GONE)
    }

    private fun showLoadingHasData() {
        progress_bar.progress = 0
        progress_bar.visibility = VISIBLE
        swipe_refresh.isRefreshing = false
        val animator = ObjectAnimator.ofInt(progress_bar, "progress", 95)
        animator.duration = 400
        animator.start()
    }

}