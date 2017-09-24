package com.jason.returnoftheping.services

import com.jason.returnoftheping.models.*
import retrofit2.Call
import retrofit2.http.*
import rx.Observable


/**
 * Created by Jason on 9/7/17.
 */
interface LOTPService {

    @GET("v1/leaderboard")
    fun getLeaderBoard(): Observable<LeaderBoard>

    @FormUrlEncoded
    @POST("v1/signin")
    fun signIn(@Field("email") email: String, @Field("password") password: String): Observable<SignInRegisterResponse>

    @POST("v1/register")
    fun register(@Body credentials: Player): Observable<Player>

    @POST("v1/logout")
    fun logOut(): Call<Player>

    @POST("v1/forgotPassword")
    fun forgotPassword(@Body email: String)

    @GET("v1/profile/{id}")
    fun getProfile(@Path("id") playerId: Long): Call<Profile>

    @GET("v1/history/{opponentId}")
    fun getHistory(@Path("opponentId") opponentId: Int): Call<Player>

    @GET("v1/players")
    fun getAllPlayers(): Call<List<Player>>

    @POST("v1/match")
    fun saveMatch(@Body match: Match): Call<String>

    @GET("v1/pendingMatches")
    fun getPendingMatches(@Path("id") playerId: Long): Call<List<Match>>

    @POST("v1/pendingMatch")
    fun confirmMatch(@Body response: MatchConfirmResponse): Call<MatchConfirmResponse>
}