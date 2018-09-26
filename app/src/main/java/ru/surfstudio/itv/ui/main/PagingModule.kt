package ru.surfstudio.itv.ui.main


import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.itv.di.scopes.ActivityScope
import ru.surfstudio.itv.network.NetworkState
import ru.surfstudio.itv.utils.Constants
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named

@Module
class PagingModule {

    @ActivityScope
    @Provides
    fun executor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @ActivityScope
    @Provides
    @Named(Constants.INITIAL_LOAD_NAME)
    fun provideInitialLoad(): BehaviorSubject<NetworkState> = BehaviorSubject.create()

    @ActivityScope
    @Provides
    @Named(Constants.NETWORK_STATE_NAME)
    fun provideNetworkState(): BehaviorSubject<NetworkState> = BehaviorSubject.create()

}