package com.jason.returnoftheping.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by Jason on 9/7/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Profile(val player: Player, val stats: Stats, val matches: List<Match> = ArrayList<Match>())