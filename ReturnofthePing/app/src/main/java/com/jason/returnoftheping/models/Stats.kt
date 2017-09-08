package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class Stats(val matchLosses: Int,
                 val gameLosses: Int,
                 val matchCount: Int,
                 val gameCount: Int,
                 val matchWinPercentage: Int,
                 val matchWins: Int,
                 val gameWins: Int,
                 val gameWinPercentage: Int) : Serializable