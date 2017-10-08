package com.jason.returnoftheping.services

import com.jason.returnoftheping.models.*
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

    @GET("v2/profile/{profileId}")
    fun getProfile(@Path("profileId") playerId: Long): Observable<Profile>

    @GET("v1/inbox")
    fun getInbox(): Observable<InboxResponse>

    @POST("v1/pendingMatch")
    @FormUrlEncoded
    fun confirmMatch(@Field("confirmed") confirmed: Boolean, @Field("matchId") matchId: Int): Observable<MatchConfirmationResponse>

    @POST("v1/grantAccess")
    fun grantAccess(@Body token: RegistrationRequest)

    @POST("v1/denyAccess")
    fun denyAccess(@Body token: RegistrationRequest)

    @POST("v1/register")
    fun register(@Body credentials: Player): Observable<Player>

    @POST("v1/logout")
    fun logOut(): Observable<Player>

    @POST("v1/forgotPassword")
    fun forgotPassword(@Body email: String)

    @GET("v1/history/{opponentId}")
    fun getHistory(@Path("opponentId") opponentId: Int): Observable<Player>

    @GET("v1/players")
    fun getAllPlayers(): Observable<ArrayList<Player>>

    @POST("v1/match")
    fun saveMatch(@Body match: Match): Observable<String>
}