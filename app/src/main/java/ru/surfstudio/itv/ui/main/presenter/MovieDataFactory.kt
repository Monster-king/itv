package ru.surfstudio.itv.ui.main.presenter

import android.arch.paging.DataSource
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.repositories.MovieRepository
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

class MovieDataFactory @Inject constructor(
        private val repository: MovieRepository,
        @param:Named(Constants.INITIAL_LOAD_NAME) private val initialLoad: BehaviorSubject<NetworkState>,
        @param:Named(Constants.NETWORK_STATE_NAME) private val networkState: BehaviorSubject<NetworkState>
) : DataSource.Factory<Int, Movie>() {

    override fun create(): DataSource<Int, Movie> {
        return MovieDataSource(repository, Executors.newSingleThreadExecutor(), initialLoad, networkState)
    }

}