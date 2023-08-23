package com.example.rawggamebase.data

import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.GameDetail
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.data.services.GamesApi
import kotlinx.coroutines.flow.flow

class GameRepository(private val api: GamesApi) {

    suspend fun getGames(searchKey: String = "") = flow<Result<List<Game>>> {
        val result = if (searchKey.isBlank()) {
            api.getGames()
        } else {
            api.searchGames(searchKey)
        }.execute()

        if (result.isSuccessful && result.body() != null) {
            emit(Result.Success(data = result.body()!!.results))
        } else {
            emit(Result.Error(NetworkClient.processError(result)))
        }
    }

    suspend fun getGameDetail(id: Int) = flow<Result<GameDetail>> {
        val result = api.getDetail(id).execute()

        if (result.isSuccessful && result.body() != null) {
            emit(Result.Success(result.body()!!))
        } else {
            emit(Result.Error(NetworkClient.processError(result)))
        }
    }
}