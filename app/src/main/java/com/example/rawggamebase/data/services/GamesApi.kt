package com.example.rawggamebase.data.services

import com.example.rawggamebase.data.dto.BaseListResponse
import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.GameDetail
import com.example.rawggamebase.utils.Config
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApi {
    @GET("games")
    suspend fun getGames(
        @Query("search") keyword: String?,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("key") apiKey: String = Config.API_KEY
    ): Response<BaseListResponse<List<Game>>>

    @GET("games/{id}")
    suspend fun getDetail(
        @Path("id") id: Int,
        @Query("key") apiKey: String = Config.API_KEY
    ): Response<GameDetail>
}