package ru.surfstudio.itv

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.surfstudio.itv.di.DaggerAppComponent


class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        appComponent.inject(this)
        return appComponent
    }

}