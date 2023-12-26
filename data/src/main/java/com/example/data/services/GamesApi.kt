package com.example.data.services

import com.example.data.Configuration
import com.example.data.Configuration.API_KEY
import com.example.data.dto.BaseListResponse
import com.example.data.dto.Game
import com.example.data.dto.GameDetail
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
        @Query("key") apiKey: String = API_KEY
    ): Response<BaseListResponse<List<Game>>>

    @GET("games/{id}")
    suspend fun getDetail(
        @Path("id") id: Int,
        @Query("key") apiKey: String = API_KEY
    ): Response<GameDetail>
}