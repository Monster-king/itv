package ru.surfstudio.itv.ui.main.presenter

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.data.cache.MovieCache
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
                                        private val dataSourceSubject: BehaviorSubject<BaseMovieDataSource>,
                                        private val repository: MovieRepository,
                                        private val cache: MovieCache,
                                        private val dataFactory: MovieDataFactory
) : BasePresenter() {
    private lateinit var dataSource: BaseMovieDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        compositeDisposable.addAll(
                dataSourceSubject.subscribe {
                    dataSource = it
                },
                view.retryClicks.mergeWith(view.retryOnError).subscribe {
                    dataSource.retryLoad()
                },
                initialLoad.observeOn(AndroidSchedulers.mainThread()).subscribe {
                    when (it) {
                        Loading -> {
                            view.showLoading()
                        }
                        is Failed -> view.showError()
                        Loaded -> view.showData()
                    }
                },
                networkState.observeOn(AndroidSchedulers.mainThread()).subscribe {
                    view.setNetworkState(it)
                    if (it is Failed) view.showSnackBar()
                },
                view.swipeRefresh.subscribe {
                    repository.refresh()
                    dataSource.invalidate()
                },
                view.favouriteClick.subscribe {
                    if (it.isFavourite)
                        cache.saveMovie(it)
                    else
                        cache.removeMovie(it)
                },
                view.searchTextChanges.subscribe {
                    dataFactory.setSearchQuery(it)
                    dataSource.invalidate()
                }
        )
    }
}