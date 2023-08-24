package com.example.rawggamebase.data

import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.GameDetail
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.data.services.GamesApi
import kotlinx.coroutines.flow.flow

class GameRepository(private val api: GamesApi) {

    suspend fun getGames(
        searchKey: String? = null,
        page: Int? = null,
        pageSize: Int = 20
    ): Result<List<Game>> {
        return try {
            val result = api.getGames(searchKey, page, pageSize)

            if (result.isSuccessful && result.body() != null) {
                Result.Success(data = result.body()!!.results)
            } else {
                Result.Error(NetworkClient.processError(result))
            }
        } catch (e: Exception) {
            Result.Error(Throwable(e.message ?: "Unknown Error"))
        }
    }

    suspend fun getGameDetail(id: Int): Result<GameDetail> {
        return try {
            val result = api.getDetail(id)

            if (result.isSuccessful && result.body() != null) {
                Result.Success(result.body()!!)
            } else {
                Result.Error(NetworkClient.processError(result))
            }
        } catch (e: Exception) {
            Result.Error(Throwable(e.message ?: "Unknown Error"))
        }
    }
}