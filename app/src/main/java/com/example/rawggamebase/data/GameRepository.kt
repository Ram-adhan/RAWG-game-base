package com.example.rawggamebase.data

import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.GameDetail
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.data.services.GamesApi
import kotlinx.coroutines.flow.flow

class GameRepository(private val api: GamesApi) {

    companion object {
        private const val PAGE_SIZE = 20
    }
    suspend fun getGames(
        searchKey: String?,
        page: Int?
    ): Result<List<Game>> {
        return try {
            val result = api.getGames(searchKey, page, PAGE_SIZE)

            if (result.isSuccessful && result.body() != null) {
                val hasNextPage = result.body()?.next != null
                Result.Success(data = result.body()!!.results, isNextPageAvailable = hasNextPage)
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