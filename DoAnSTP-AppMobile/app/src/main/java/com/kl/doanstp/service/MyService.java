package com.kl.doanstp.service;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyService {
//    @POST("/")
//    @Headers("Content-Type: application/json")
//    Call<JsonObject> (@Body JSONObject data);

    @POST("/login")
    @Headers("Content-Type: application/json")
    Call<JsonObject> login(@Body JSONObject data);

    @POST("/register")
    @Headers("Content-Type: application/json")
    Call<JsonObject> register(@Body JSONObject data);

    @POST("/newteam")
    @Headers("Content-Type: application/json")
    Call<JsonObject> createNewTeam(@Body JSONObject data);

    @POST("/getplayer")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getPlayer(@Body JSONObject data);

    @POST("/getteamplayers")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getTeamPlayers(@Body JSONObject data);

    @POST("/applytoteam")
    @Headers("Content-Type: application/json")
    Call<JsonObject> applyToTeam(@Body JSONObject data);

    @POST("/cancelapply")
    @Headers("Content-Type: application/json")
    Call<JsonObject> cancelApply(@Body JSONObject data);

    @POST("/getapplication")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getApplication (@Body JSONObject data);

    @POST("/rejectapply")
    @Headers("Content-Type: application/json")
    Call<JsonObject> rejectApplicant(@Body JSONObject data);

    @POST("/acceptapply")
    @Headers("Content-Type: application/json")
    Call<JsonObject> acceptApplicant(@Body JSONObject data);

    @POST("/getapplicants")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getApplicants(@Body JSONObject data);

    @POST("/creatematch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> createMatch(@Body JSONObject data);

    @POST("/searchformatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> searchForMatches(@Body JSONObject data);

    @POST("/searchhostmatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> searchForHotMatches(@Body JSONObject data);

    @POST("/searchinvitedmatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> searchForInvitedMatches(@Body JSONObject data);

    @POST("/joinmatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> joinMatch(@Body JSONObject data);

    @POST("/invite")
    @Headers("Content-Type: application/json")
    Call<JsonObject> invite(@Body JSONObject data);

    @POST("/acceptmatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> acceptMatch(@Body JSONObject data);

    @POST("/denymatch")
    @Headers("Content-Type: application/json")
    Call<JsonObject> denyMatch(@Body JSONObject data);

    @POST("/updateregid")
    @Headers("Content-Type: application/json")
    Call<JsonObject> updateRegID(@Body JSONObject data);
}
