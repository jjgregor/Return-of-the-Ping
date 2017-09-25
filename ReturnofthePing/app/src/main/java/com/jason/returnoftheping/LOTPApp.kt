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
    private var currentPlayer: Player? = null

    companion object {
        @JvmStatic
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(getString(R.string.api_domain)))
                .build()

        component.inject(this)
        currentPlayer = Preferences().getCurrentPlayer(this)
        Log.d(TAG, "CurrentPlayer: " + currentPlayer)

        Stetho.initializeWithDefaults(this)
    }


    fun getCurrentPlayer() = currentPlayer

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }


}