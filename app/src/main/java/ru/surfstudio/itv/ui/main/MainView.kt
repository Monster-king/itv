package ru.surfstudio.itv.ui.main

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.base.BaseViewInterface

interface MainView : BaseViewInterface<View> {
    val retryClicks: PublishSubject<Any>
    val retryOnError: Observable<Any>
    val swipeRefresh: Observable<Any>
    val favouriteClick: PublishSubject<Movie>
    val searchTextChanges: Observable<String>

    fun setNetworkState(networkState: NetworkState)

    fun showData()

    fun showError()

    fun showSnackBar()

    fun showLoading()

    fun getListPosition(): Int

    fun setPagedListLiveData(pagedList: LiveData<PagedList<Movie>>)

    fun getSearchQuery(): String

    fun showEmptyDataView(searchQuery: String)
}