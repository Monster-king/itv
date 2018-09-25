package ru.surfstudio.itv.ui.main.presenter

import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.Failed
import ru.surfstudio.itv.network.Loaded
import ru.surfstudio.itv.network.Loading
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.Error
import ru.surfstudio.itv.repositories.MovieRepoResult
import ru.surfstudio.itv.repositories.NoInternet
import ru.surfstudio.itv.repositories.Success
import ru.surfstudio.itv.ui.base.BaseDataSource
import java.util.concurrent.Executor

abstract class BaseMovieDataSource(retryExecutor: Executor,
                                   protected val networkState: BehaviorSubject<NetworkState>
) : BaseDataSource<Int, Movie>(retryExecutor) {

    protected abstract fun getData(page: Int): MovieRepoResult


    protected fun load(page: Int, nextPage: Int, callback: LoadCallback<Int, Movie>) {
        networkState.onNext(Loading)
        val result = getData(page)
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