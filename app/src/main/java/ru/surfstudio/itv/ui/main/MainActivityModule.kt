package ru.surfstudio.itv.ui.main

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.surfstudio.itv.ui.main.presenter.MainPresenter



@Module
abstract class MainActivityModule {

    @Provides
    fun provideMainPresenter(mainView: MainView): MainPresenter {
        return MainPresenter()
    }

    @Binds
    abstract fun provideMainView(activity: MainActivity): MainView
}