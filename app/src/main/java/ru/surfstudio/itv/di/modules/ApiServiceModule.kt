package ru.surfstudio.itv.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.itv.network.MovieService
import ru.surfstudio.itv.utils.Constants
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiServiceModule {

    @Singleton
    @Provides
    fun provideService(client: Retrofit): MovieService {
        return client.create(MovieService::class.java)
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun providesRetrofitClient(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
    }
}