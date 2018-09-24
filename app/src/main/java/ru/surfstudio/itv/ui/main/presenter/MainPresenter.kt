package ru.surfstudio.itv.ui.main.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.network.Failed
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.MovieRepository
import ru.surfstudio.itv.ui.base.BasePresenter
import ru.surfstudio.itv.ui.main.MainView
import ru.surfstudio.itv.utils.Constants
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainPresenter @Inject constructor(private val view: MainView,
                                        @param:Named(Constants.INITIAL_LOAD_NAME)
                                        private val initialLoad: BehaviorSubject<NetworkState>,
                                        @param:Named(Constants.NETWORK_STATE_NAME)
                                        private val networkState: BehaviorSubject<NetworkState>,
                                        private val dataSourceSubject: BehaviorSubject<MovieDataSource>,
                                        private val repository: MovieRepository
) : BasePresenter() {
    private lateinit var dataSource: MovieDataSource
    private var notShowLoading = false

    override fun onCreate() {
        compositeDisposable.addAll(
                dataSourceSubject.subscribe {
                    dataSource = it
                },
                view.retryClicks.mergeWith(view.retryOnError).subscribe {
                    dataSource.retryAllFailed()
                },
                initialLoad.observeOn(AndroidSchedulers.mainThread()).filter {
                    val res = !(it == Loading && notShowLoading)
                    if (!res) notShowLoading = false
                    return@filter res
                }.subscribe {
                    when (it) {
                        Loading -> view.showInitialLoading()
                        is Failed -> view.showError()
                        Loaded -> view.showData()
                    }
                },
                networkState.observeOn(AndroidSchedulers.mainThread()).subscribe {
                    view.setNetworkState(it)
                },
                view.swipeRefresh.subscribe {
                    notShowLoading = true
                    repository.refresh()
                    dataSource.invalidate()
                }
        )
    }
}