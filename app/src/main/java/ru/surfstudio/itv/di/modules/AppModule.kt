package ru.surfstudio.itv.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import ru.surfstudio.itv.utils.Constants
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    @Named(Constants.APP_CONTEXT)
    abstract fun provideContext(application: Application): Context

}