package com.jason.returnoftheping

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.jason.returnoftheping.dagger.AppComponent
import com.jason.returnoftheping.dagger.AppModule
import com.jason.returnoftheping.dagger.DaggerAppComponent
import com.jason.returnoftheping.dagger.NetworkModule
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.preferences.Preferences


/**
 * Created by Jason on 9/7/17.
 */
class LOTPApp : Application() {

    private val TAG = LOTPApp::class.java.simpleName

    companion object {
        @JvmStatic
        lateinit var component: AppComponent
        var currentPlayer: Player? = null
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(getString(R.string.api_domain), applicationContext))
                .build()

        component.inject(this)
        Companion.currentPlayer = Preferences().getCurrentPlayer(this)
        Log.d(TAG, "CurrentPlayer: " + Companion.currentPlayer)

        Stetho.initializeWithDefaults(this)
    }


    fun getCurrentPlayer() = Companion.currentPlayer

    fun setCurrentPlayer(player: Player) {
        Companion.currentPlayer = player
    }


}