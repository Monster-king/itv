package ru.surfstudio.itv.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.surfstudio.itv.ui.main.MainActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity
}