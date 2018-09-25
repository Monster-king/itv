package ru.surfstudio.itv.ui.main

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

    fun setNetworkState(networkState: NetworkState)

    fun showData()

    fun showInitialLoading()

    fun showError()

    fun showSnackBar()

    fun showLoading()
}