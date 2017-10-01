package com.jason.returnoftheping.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by Jason on 9/24/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SignInRegisterResponse(val accessToken: String,
                                  val player: Player,
                                  val matches: List<Match>,
                                  val stats: Stats)