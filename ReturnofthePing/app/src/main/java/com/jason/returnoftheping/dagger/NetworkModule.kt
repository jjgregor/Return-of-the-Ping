package com.jason.returnoftheping.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jason.returnoftheping.preferences.Preferences
import com.jason.returnoftheping.util.ObjectMapperFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


/**
 * Created by Jason on 9/24/17.
 */
@Module
class NetworkModule(internal val mBaseUrl: String, internal val context: Context) {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(application.cacheDir, cacheSize.toLong())
        return cache
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("X-LOTP-API-KEY", "e7ee2d6e0118578da9ada86e8c93ba3313793a0cf7e0b333fa90b996b1fc9581")
                    Preferences().getAccessToken(context)?.let { ongoing.addHeader("X-LOTP-ACCESS-TOKEN", it) }
                    chain.proceed(ongoing.build())
                }
                .addNetworkInterceptor(StethoInterceptor())
                .cache(cache)
                .build()
        return client
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(mBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperFactory().getObjectMapper()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit
    }
}