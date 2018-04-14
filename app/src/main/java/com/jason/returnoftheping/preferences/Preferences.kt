package com.jason.returnoftheping.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.fasterxml.jackson.core.JsonProcessingException
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.util.ObjectMapperFactory
import java.io.IOException


/**
 * Created by Jason on 9/17/17.
 */
class Preferences {

    private val TAG = Preferences::class.java.simpleName
    private val SHARED_PREFS = "ping_pong_shared_prefs"
    private val CURRENT_PLAYER = "current_player"
    private val ACCESS_TOKEN = "access_token"

    fun getCurrentPlayer(context: Context): Player? {
        val player = getSharedPreferences(context).getString(CURRENT_PLAYER, null)
        if (!player.isNullOrEmpty()) {
            try {
                return ObjectMapperFactory().getObjectMapper().readValue(player, Player::class.java)
            } catch (e: IOException) {
                Log.e(TAG, "Error getting current player", e)
            }

        }
        return null
    }

    fun setCurrentPlayer(player: Player, context: Context): Boolean {
        try {
            getSharedPreferences(context)
                    .edit()
                    .putString(CURRENT_PLAYER, ObjectMapperFactory().getObjectMapper().writeValueAsString(player))
                    .apply()
            return true
        } catch (e: JsonProcessingException) {
            Log.e(TAG, "Error setting current player", e)
            return false
        }
    }

    fun getAccessToken(context: Context): String? {
        val token = getSharedPreferences(context).getString(ACCESS_TOKEN, null)
        if (!token.isNullOrEmpty()) {
            try {
                return token
            } catch (e: IOException) {
                Log.e(TAG, "Error getting access token ", e)
            }
        }
        return null
    }

    fun setAccessToken(accessToken: String, context: Context): Boolean {
        try {
            getSharedPreferences(context)
                    .edit()
                    .putString(ACCESS_TOKEN, accessToken)
                    .apply()
            return true
        } catch (e: JsonProcessingException) {
            Log.e(TAG, "Error setting current player", e)
            return false
        }
    }

    fun signOut(context: Context) {
        getSharedPreferences(context).edit().remove(CURRENT_PLAYER).apply()
    }

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }
}