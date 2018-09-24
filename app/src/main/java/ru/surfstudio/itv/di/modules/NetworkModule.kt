package ru.surfstudio.itv.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.itv.utils.Constants
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun okhtppLoggingInterceptor() = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)!!

    @Singleton
    @Provides
    fun okhttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", Constants.API_KEY)
                        .addQueryParameter("language", Constants.LANGUAGE)
                        .build()
                val requestBuilder = original.newBuilder()
                        .url(url)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()!!
}