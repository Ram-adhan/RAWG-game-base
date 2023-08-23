package com.example.rawggamebase.utils

import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.services.GamesApi
import retrofit2.Retrofit

class RepositoryModule(
    private val retrofit: Retrofit,
) {
    fun getGamesRepository(): GameRepository = GameRepository(retrofit.create(GamesApi::class.java))
}