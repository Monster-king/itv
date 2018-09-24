package ru.surfstudio.itv.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.view.View
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.model.Movie
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.ui.adapters.MovieAdapter
import ru.surfstudio.itv.ui.base.BasePresenter
import ru.surfstudio.itv.ui.base.BaseViewInterface
import ru.surfstudio.itv.ui.main.presenter.MainPresenter
import ru.surfstudio.itv.ui.main.presenter.MovieDataFactory
import ru.surfstudio.itv.ui.main.presenter.MovieDataSource
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executors
import javax.inject.Named


@Module
abstract class MainActivityModule {

    @ActivityScope
    @Binds
    abstract fun bindMainView(mainView: MainViewImpl): MainView

    @ActivityScope
    @Binds
    abstract fun bindLifecycleOwner(activity: MainActivity): LifecycleOwner

    @ActivityScope
    @Binds
    abstract fun bindContext(activity: MainActivity): Context

    @ActivityScope
    @Binds
    abstract fun bindPresenter(presenter: MainPresenter): BasePresenter

    @ActivityScope
    @Binds
    abstract fun bindView(mainView: MainView): BaseViewInterface<View>

}
