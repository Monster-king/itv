package ru.surfstudio.itv.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.ui.main.MainActivity
import ru.surfstudio.itv.ui.main.modules.MainActivityModule
import ru.surfstudio.itv.ui.main.modules.PagingModule

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, PagingModule::class])
    abstract fun bindMainActivity(): MainActivity
}