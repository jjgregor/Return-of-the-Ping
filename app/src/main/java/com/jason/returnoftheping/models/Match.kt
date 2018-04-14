package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class Match(val opponentId: Int,
                 val opponentName: String?,
                 val wins: Int,
                 val losses: Int,
                 val date: String,
                 val id: Int,
                 val isWin: Boolean) : Serializable