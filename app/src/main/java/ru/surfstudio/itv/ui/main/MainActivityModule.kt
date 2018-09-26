package ru.surfstudio.itv.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.view.View
import dagger.Binds
import dagger.Module
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.ui.adapters.MovieAdapter
import ru.surfstudio.itv.ui.base.BasePresenter
import ru.surfstudio.itv.ui.base.BaseViewInterface
import ru.surfstudio.itv.ui.main.presenter.MainPresenter


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
    abstract fun bindView(mainView: MainViewImpl): BaseViewInterface<View>

}
