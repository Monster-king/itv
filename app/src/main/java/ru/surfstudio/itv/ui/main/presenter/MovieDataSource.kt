package ru.surfstudio.itv.ui.main.presenter

import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.*
import ru.surfstudio.itv.repositories.*
import ru.surfstudio.itv.ui.base.BaseDataSource
import java.util.concurrent.Executor

class MovieDataSource(
        private val repository: MovieRepository,
        retryExecutor: Executor,
        private val initialLoading: BehaviorSubject<NetworkState>,
        private val networkState: BehaviorSubject<NetworkState>,
        private val searchQuery: String
) : BaseDataSource<Movie>(retryExecutor) {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        Log.i("initialLoading", "${params.requestedStartPosition}")
        initialLoading.onNext(Loading)
        networkState.onNext(Loading)
        val result = repository.getMovies(params.requestedStartPosition, params.requestedLoadSize, searchQuery)
        when (result) {
            is Success -> {
                if (result.movies.isNotEmpty()) {
                    initialLoading.onNext(Loaded)
                } else {
                    initialLoading.onNext(Empty(searchQuery))
                }
                networkState.onNext(Loaded)
                callback.onResult(result.movies, result.startPosition)
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

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        load(params, callback)
    }

    private fun load(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        networkState.onNext(Loading)
        val result = repository.getMovies(params.startPosition, params.loadSize, searchQuery)
        when (result) {
            is Success -> {
                networkState.onNext(Loaded)
                callback.onResult(result.movies)
            }
            is Error -> {
                networkState.onNext(Failed(result.message))
                retry = {
                    load(params, callback)
                }
            }
            is NoInternet -> {
                networkState.onNext(Failed("No internet")) // todo when you must show this message in ui, replace it with string res
                retry = {
                    load(params, callback)
                }
            }
        }
    }

}