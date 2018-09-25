package ru.surfstudio.itv.di.modules

import dagger.Binds
import dagger.Module
import ru.surfstudio.itv.data.cache.MovieCache
import ru.surfstudio.itv.data.cache.SharedPrefCache
import javax.inject.Singleton

@Module
abstract class CacheModule {

    @Singleton
    @Binds
    abstract fun bindsCache(prefCache: SharedPrefCache): MovieCache

}