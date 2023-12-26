package com.example.data

import com.example.data.services.GamesApi
import retrofit2.Retrofit

class RepositoryModule(
    private val retrofit: Retrofit,
) {
    fun getGamesRepository(): GameRepository =
        GameRepository(retrofit.create(GamesApi::class.java))
}