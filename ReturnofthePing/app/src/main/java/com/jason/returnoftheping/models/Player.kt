package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class Player(
        var id: String,
        var name: String,
        var email: String,
        var password: String,
        val pushNotificationsEnabled: Boolean = true,
        val emailNotificationsEnabled: Boolean = true,
        var rating: String,
        var avatarUrl: String) : Serializable