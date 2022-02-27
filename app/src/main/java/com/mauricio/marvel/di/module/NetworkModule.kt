package com.mauricio.marvel.di.module

import android.app.Application
import android.content.Context
import com.mauricio.marvel.BuildConfig
import com.mauricio.marvel.network.HttpHeadersInterceptor
import com.mauricio.marvel.network.RetrofitApiService
import com.mauricio.marvel.utils.network.ConnectionNetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://gateway.marvel.com:443/"

    @Singleton
    @Provides
    fun provideHeadersInterceptor(): HttpHeadersInterceptor = HttpHeadersInterceptor()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        headersInterceptor: HttpHeadersInterceptor,
        cache: Cache,
        connectionNetwork: ConnectionNetworkUtils
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headersInterceptor)
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (connectionNetwork.isOnline())
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else {
                val maxStale = 60 * 60 * 24 * 1 // Offline cache available for 1 day
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=$maxStale").build()
            }
            chain.proceed(request)
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RetrofitApiService = retrofit.create(RetrofitApiService::class.java)

    @Singleton
    @Provides
    fun provideCacheFile(context: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        return Cache(context.cacheDir, cacheSize)
    }

    @Singleton
    @Provides
    fun provideConnectionNetworkUtils(@ApplicationContext appContext: Context): ConnectionNetworkUtils {
        return ConnectionNetworkUtils(appContext)
    }
}

