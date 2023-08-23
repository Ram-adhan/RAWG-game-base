package com.example.rawggamebase.data

import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.data.services.GamesApi
import kotlinx.coroutines.flow.flow

class GameRepository(private val api: GamesApi) {

    suspend fun getGames(searchKey: String = "") = flow<Result<List<Game>>> {
        val result = if (searchKey.isBlank()) {
            api.getGames().execute()
        } else {
            api.searchGames(searchKey).execute()
        }

        if (result.isSuccessful && result.body() != null) {
            emit(Result.Success(data = result.body()!!.results))
        } else {
            val errorData = if (result.body() == null) {
                Throwable("Null Data")
            } else {
                Throwable(result.errorBody()?.string())
            }
            emit(Result.Error(errorData))
        }

    }
}