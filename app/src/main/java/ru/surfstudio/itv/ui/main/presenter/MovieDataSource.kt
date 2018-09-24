package ru.surfstudio.itv.ui.main.presenter

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.Failed
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.Error
import ru.surfstudio.itv.repositories.MovieRepository
import ru.surfstudio.itv.repositories.NoInternet
import ru.surfstudio.itv.repositories.Success
import java.util.concurrent.Executor

class MovieDataSource(
        private val repository: MovieRepository,
        private val retryExecutor: Executor,
        private val initialLoading: BehaviorSubject<NetworkState>,
        private val networkState: BehaviorSubject<NetworkState>
) : PageKeyedDataSource<Int, Movie>() {

    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        Log.i("loadingPage", "initial")

        initialLoading.onNext(Loading)
        networkState.onNext(Loading)
        val result = repository.getMovies(1)
        when (result) {
            is Success -> {
                initialLoading.onNext(Loaded)
                networkState.onNext(Loaded)
                callback.onResult(result.movies, null, 2)
            }
            is Error -> {
                initialLoading.onNext(Failed(result.message))
                networkState.onNext(Failed(result.message))
                retry = {
                    loadInitial(params, callback)
                }
            }
            is NoInternet -> {
                initialLoading.onNext(Failed("No internet"))
                networkState.onNext(Failed("No internet")) // todo when you must show this message in ui, replace it with string res
                retry = {
                    loadInitial(params, callback)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        load(params.key, params.key + 1, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        load(params.key, params.key - 1, callback)
    }

    private fun load(page: Int, nextPage: Int, callback: LoadCallback<Int, Movie>) {
        Log.i("loadingPage", "$page")
        networkState.onNext(Loading)
        val result = repository.getMovies(page)
        when (result) {
            is Success -> {
                networkState.onNext(Loaded)
                callback.onResult(result.movies, nextPage)
            }
            is Error -> {
                networkState.onNext(Failed(result.message))
                retry = {
                    load(page, nextPage, callback)
                }
            }
            is NoInternet -> {
                networkState.onNext(Failed("No internet")) // todo when you must show this message in ui, replace it with string res
                retry = {
                    load(page, nextPage, callback)
                }
            }
        }
    }

}