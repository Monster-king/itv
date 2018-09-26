package ru.surfstudio.itv.ui.main.presenter

import android.arch.paging.DataSource
import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.MovieRepository
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MovieDataFactory @Inject constructor(
        private val repository: MovieRepository,
        @param:Named(Constants.INITIAL_LOAD_NAME) private val initialLoad: BehaviorSubject<NetworkState>,
        @param:Named(Constants.NETWORK_STATE_NAME) private val networkState: BehaviorSubject<NetworkState>,
        private val fetchExecutor: Executor
) : DataSource.Factory<Int, Movie>() {

    private var searchQuery = ""
    var dataSource: MovieDataSource? = null

    fun setSearchQuery(searchQuery: String) {
        if (searchQuery == this.searchQuery) return
        this.searchQuery = searchQuery
        dataSource?.invalidate()
    }

    override fun create(): DataSource<Int, Movie> {
        dataSource =
                MovieDataSource(repository, fetchExecutor, initialLoad, networkState, searchQuery)
        return dataSource!!
    }

}