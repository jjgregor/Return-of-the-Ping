package com.jason.returnoftheping.models

/**
 * Created by Jason on 9/7/17.
 */
data class Profile(val player: Player, val stats: Stats, val matches: List<Match> = ArrayList<Match>())