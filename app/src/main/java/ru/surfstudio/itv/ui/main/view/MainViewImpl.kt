package ru.surfstudio.itv.ui.main.view

import android.animation.ObjectAnimator
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.empty_result_layout.view.*
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
                                       private val lifecycleOwner: LifecycleOwner,
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
        updateViews(content = VISIBLE)
    }

    override fun showError() {
        updateViews(error = VISIBLE)
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

    override fun getListPosition(): Int {
        val offset = adapter.currentList?.positionOffset ?: 0
        var position = (movies_rv.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        if (position < 0)
            position = 0
        return offset + position

    }

    override fun setPagedListLiveData(pagedList: LiveData<PagedList<Movie>>) {
        pagedList.observe(lifecycleOwner, Observer<PagedList<Movie>> { movies ->
            adapter.submitList(movies)
        })
    }

    override fun getSearchQuery(): String {
        return search_tv.text.toString()
    }

    override fun showEmptyDataView(searchQuery: String) {
        empty_message_tv.text = String.format(resources.getString(R.string.not_found_text), searchQuery)
        updateViews(empty = VISIBLE)
    }

    private fun showInitialLoading() {
        updateViews(initialProgress = VISIBLE)
    }

    private fun updateViews(content: Int = GONE, initialProgress: Int = GONE, error: Int = GONE,
                            progress: Int = GONE, empty: Int = GONE) {
        swipe_refresh.visibility = content
        initial_progress.visibility = initialProgress
        error_layout.visibility = error
        progress_bar.visibility = progress
        empty_result_layout.visibility = empty
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