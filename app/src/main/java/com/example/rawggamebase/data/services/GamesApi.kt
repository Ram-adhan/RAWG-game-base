package com.example.rawggamebase.data.services

import com.example.rawggamebase.data.dto.BaseResponse
import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.utils.Config
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApi {
    @GET("games")
    fun getGames(@Query("key") apiKey: String = Config.API_KEY): Call<BaseResponse<List<Game>>>

    @GET("games")
    fun searchGames(
        @Query("search") keyword: String,
        @Query("key") apiKey: String = Config.API_KEY
    ): Call<BaseResponse<List<Game>>>
}