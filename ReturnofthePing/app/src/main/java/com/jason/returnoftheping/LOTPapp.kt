package com.jason.returnoftheping

import android.app.Application
import com.facebook.stetho.Stetho
import retrofit2.converter.jackson.JacksonConverterFactory
import android.provider.Telephony.Carriers.SERVER
import android.util.Log
import retrofit2.Retrofit
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.services.LOTPservice
import okhttp3.OkHttpClient



/**
 * Created by Jason on 9/7/17.
 */
class LOTPapp : Application() {

    private lateinit var currentPlayer: Player

    override fun onCreate() {
        super.onCreate()
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        val retrofit = Retrofit.Builder().baseUrl(LOTPservice.SERVER)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperFactory.getObjectMapper()))
                .client(client)
                .build()

        service = retrofit.create(LOTPservice::class.java)
        mCurrentPlayer = PingPongPreferences.getCurrentPlayer(this)
        Log.d(TAG, "CurrentPlayer: " + mCurrentPlayer)

        Stetho.initializeWithDefaults(this)
    }

    fun getPingPongService(): LOTPService {
        return service
    }

    fun getCurrentPlayer(): Player {
        return currentPlayer
    }

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }

}