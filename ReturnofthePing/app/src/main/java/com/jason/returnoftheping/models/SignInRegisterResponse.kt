package com.jason.returnoftheping.models

/**
 * Created by Jason on 9/24/17.
 */
data class SignInRegisterResponse(val accessToken: String?,
                                  val player: Player,
                                  val matches: List<Match>,
                                  val stats: Stats)