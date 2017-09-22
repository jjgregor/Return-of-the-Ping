package com.jason.returnoftheping

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.preferences.Preferences
import com.jason.returnoftheping.services.LOTPService
import com.jason.returnoftheping.util.ObjectMapperFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


/**
 * Created by Jason on 9/7/17.
 */
class LOTPApp : Application() {

    private val TAG = LOTPApp::class.java.simpleName
    private var currentPlayer: Player? = null
    private lateinit var service: LOTPService

    override fun onCreate() {
        super.onCreate()
        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("X-LOTP-API-KEY", "e7ee2d6e0118578da9ada86e8c93ba3313793a0cf7e0b333fa90b996b1fc9581")
                    chain.proceed(ongoing.build())
                }
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        val retrofit = Retrofit.Builder().baseUrl(getString(R.string.api_domain))
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperFactory().getObjectMapper()))
                .client(client)
                .build()

        service = retrofit.create(LOTPService::class.java)
        currentPlayer = Preferences().getCurrentPlayer(this)
        Log.d(TAG, "CurrentPlayer: " + currentPlayer)

        Stetho.initializeWithDefaults(this)
    }

    fun getPingPongService() = service

    fun getCurrentPlayer() = currentPlayer

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }

}