package com.jason.returnoftheping.models

import java.io.Serializable

/**
 * Created by Jason on 9/7/17.
 */
data class LeaderBoardItem(var rating: Int,
                           var wins: Int,
                           var name: String,
                           var profileId: Int,
                           var losses: Int,
                           var rank: Int,
                           var gravatarImageUrl: String?) : Serializable