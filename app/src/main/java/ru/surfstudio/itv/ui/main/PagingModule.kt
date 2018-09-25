package ru.surfstudio.itv.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.data.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.adapters.MovieAdapter
import ru.surfstudio.itv.ui.main.presenter.MovieDataFactory
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executors
import javax.inject.Named

@Module
class PagingModule {

    @ActivityScope
    @Provides
    fun config(): PagedList.Config {
        return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(30)
                .build()
    }

    @ActivityScope
    @Provides
    fun pagedList(sourceFactory: MovieDataFactory, config: PagedList.Config): LiveData<PagedList<Movie>> {
        return LivePagedListBuilder<Int, Movie>(sourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build()
    }

    @ActivityScope
    @Provides
    fun provideMovieAdapter(pagedList: LiveData<PagedList<Movie>>,
                            lifecycle: LifecycleOwner
    ): MovieAdapter {
        val adapter = MovieAdapter()
        pagedList.observe(lifecycle, Observer<PagedList<Movie>> { movies ->
            adapter.submitList(movies)
        })
        return adapter
    }

    @ActivityScope
    @Provides
    @Named(Constants.INITIAL_LOAD_NAME)
    fun provideInitialLoad(): BehaviorSubject<NetworkState> = BehaviorSubject.create()

    @ActivityScope
    @Provides
    @Named(Constants.NETWORK_STATE_NAME)
    fun provideNetworkState(): BehaviorSubject<NetworkState> = BehaviorSubject.create()

    @ActivityScope
    @Provides
    fun provideDataSourceSubject(dataFactory: MovieDataFactory) = dataFactory.dataSourceSubject

}