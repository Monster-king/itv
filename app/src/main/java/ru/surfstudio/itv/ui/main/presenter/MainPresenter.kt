package ru.surfstudio.itv.ui.main.presenter

import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.data.cache.MovieCache
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.network.*
import ru.surfstudio.itv.repositories.MovieRepository
import ru.surfstudio.itv.ui.base.BasePresenter
import ru.surfstudio.itv.ui.main.view.MainView
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainPresenter @Inject constructor(private val view: MainView,
                                        @param:Named(Constants.INITIAL_LOAD_NAME)
                                        private val initialLoad: BehaviorSubject<NetworkState>,
                                        @param:Named(Constants.NETWORK_STATE_NAME)
                                        private val networkState: BehaviorSubject<NetworkState>,
                                        private val repository: MovieRepository,
                                        private val cache: MovieCache,
                                        private val dataFactory: MovieDataFactory
) : BasePresenter() {

    companion object {
        private const val LIST_POSITION = "position"
        private const val SEARCH_QUERY = "search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val position = savedInstanceState?.getInt(LIST_POSITION) ?: 0
        val searchQuery = savedInstanceState?.getString(SEARCH_QUERY) ?: ""
        initPagedList(position, searchQuery)
        compositeDisposable.addAll(
                view.retryClicks.mergeWith(view.retryOnError).subscribe {
                    dataFactory.dataSource?.retryLoad()
                },
                initialLoad.observeOn(AndroidSchedulers.mainThread()).subscribe {
                    when (it) {
                        Loading -> {
                            view.showLoading()
                        }
                        is Failed -> view.showError()
                        Loaded -> view.showData()
                        is Empty -> view.showEmptyDataView(it.searchQuery)
                    }
                },
                networkState.observeOn(AndroidSchedulers.mainThread()).subscribe {
                    view.setNetworkState(it)
                    if (it is Failed) view.showSnackBar()
                },
                view.swipeRefresh.subscribe {
                    repository.refresh()
                    dataFactory.dataSource?.invalidate()
                },
                view.favouriteClick.subscribe {
                    if (it.isFavourite)
                        cache.saveMovie(it)
                    else
                        cache.removeMovie(it)
                },
                view.searchTextChanges.subscribe {
                    dataFactory.setSearchQuery(it)
                }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(LIST_POSITION, view.getListPosition())
        outState.putString(SEARCH_QUERY, view.getSearchQuery())
    }

    private fun initPagedList(position: Int, searchQuery: String) {
        Log.i("savedPos", "$position")
        dataFactory.setSearchQuery(searchQuery)

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Constants.LOAD_SIZE)
                .build()

        val pagedList = LivePagedListBuilder<Int, Movie>(dataFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setInitialLoadKey(position)
                .build()

        view.setPagedListLiveData(pagedList)
    }
}