package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class Player(
        var id: Int,
        var name: String,
        var email: String?,
        val pushNotificationsEnabled: Boolean = true,
        val emailNotificationsEnabled: Boolean = true,
        var rating: String?,
        var gravatarImageUrl: String?) : Serializable