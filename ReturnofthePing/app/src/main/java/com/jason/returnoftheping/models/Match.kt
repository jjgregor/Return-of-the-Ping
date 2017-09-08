package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class Match(val playerOneId: Int,
                 val playerTwoWins: Int,
                 val playerOneWins: Int,
                 val date: String,
                 val id: Int,
                 val playerOneName: String,
                 val playerTwoName: String) : Serializable
