package com.jason.returnoftheping.services

import com.jason.returnoftheping.models.Match
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.models.Profile
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Jason on 9/7/17.
 */
interface LOTPservice {

    var SERVER = "https://zappos.lordoftheping.com/"

    @GET("tt/leaderboard")
    fun getLeaderBoard(): Call<List<LeaderboardItem>>

    @Headers("Content-Type: application/json")
    @POST("tt/signin")
    fun signIn(@Body credentials: Player): Call<Player>

    @Headers("Content-Type: application/json")
    @POST("tt/register")
    fun register(@Body credentials: Player): Call<Player>

    @GET("tt/profile/{id}")
    fun getProfile(@Path("id") playerId: Long): Call<Profile>

    @GET("tt/players")
    fun getAllPlayers(): Call<List<Player>>

    @POST("tt/saveMatch")
    fun saveMatch(@Body match: Match): Call<String>

    @GET("tt/pending/player/{id}")
    fun getPendingMatches(@Path("id") playerId: Long): Call<List<Match>>

    @POST("tt/confirmMatch")
    fun confirmMatch(@Body response: MatchConfirmResponse): Call<MatchConfirmResponse>
}