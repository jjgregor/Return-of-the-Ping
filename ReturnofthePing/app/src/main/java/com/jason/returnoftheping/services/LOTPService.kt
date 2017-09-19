package com.jason.returnoftheping.services

import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.*
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Jason on 9/7/17.
 */
interface LOTPService {

    @GET("/v1/leaderboard")
    @Headers(Constants.X_LOTP_API_KEY)
    fun getLeaderBoard(): Call<List<LeaderBoardItem>>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/signin")
    fun signIn(@Body credentials: Player): Call<Player>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/register")
    fun register(@Body credentials: Player): Call<Player>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/logout")
    fun logOut(): Call<Player>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/forgotPassword")
    fun forgotPassword(@Body email: String)

    @Headers(Constants.X_LOTP_API_KEY)
    @GET("/v1/profile/{id}")
    fun getProfile(@Path("id") playerId: Long): Call<Profile>

    @Headers(Constants.X_LOTP_API_KEY)
    @GET("/v1/history/{opponentId}")
    fun getHistory(@Path("opponentId") opponentId: Int): Call<Player>

    @Headers(Constants.X_LOTP_API_KEY)
    @GET("/v1/players")
    fun getAllPlayers(): Call<List<Player>>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/match")
    fun saveMatch(@Body match: Match): Call<String>

    @Headers(Constants.X_LOTP_API_KEY)
    @GET("/v1/pendingMatches")
    fun getPendingMatches(@Path("id") playerId: Long): Call<List<Match>>

    @Headers(Constants.X_LOTP_API_KEY)
    @POST("/v1/pendingMatch")
    fun confirmMatch(@Body response: MatchConfirmResponse): Call<MatchConfirmResponse>
}