package ru.surfstudio.itv.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.surfstudio.itv.MainActivity

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity

}