package ru.surfstudio.itv.ui.main.presenter

import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.Failed
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.*
import java.util.concurrent.Executor

class SearchMovieDataSource(
        private val repository: MovieRepository,
        retryExecutor: Executor,
        private val initialLoading: BehaviorSubject<NetworkState>,
        networkState: BehaviorSubject<NetworkState>,
        private val searchQuery: String
) : BaseMovieDataSource(retryExecutor, networkState) {

    override fun getData(page: Int): MovieRepoResult {
        return repository.searchMovies(searchQuery, page)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        Log.i("loadingPage", "initial")
        initialLoading.onNext(Loading)
        networkState.onNext(Loading)
        val result = repository.searchMovies(searchQuery)
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
        // we set null to previousKey on loadInitial, that's why this method never be called
    }

}