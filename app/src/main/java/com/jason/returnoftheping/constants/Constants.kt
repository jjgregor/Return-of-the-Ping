package com.jason.returnoftheping.constants

/**
 * Created by Jason on 9/17/17.
 */
object Constants {
    val EXTRA_LEADER_BOARD = "extra-leader-board"
    val EMAIL = "email"
    val PASSWORD = "password"
    val PLAYER = "player"
    val STATE_PROFILE = "profile"
    val MATCHES = "matches"
    val STATS = "stats"
    val PROFILE = "profile"
    val VIEW_PAGER_OUT = "view-pager-out"


    val WINS_ARRAY = arrayOf("0", "1", "2", "3", "4", "5")

    val LOSSES_ARRAYS = arrayOf(

            // 0 WINS (loss)
            arrayOf("3", "4", "5"),

            // 1 WIN (loss)
            arrayOf("3", "4"),

            // 2 WINS (loss)
            arrayOf("3"),

            // 3 WINS (win)
            arrayOf("0", "1", "2"),

            // 4 WINS (win)
            arrayOf("0", "1"),

            // 5 WINS (win)
            arrayOf("0"))
}