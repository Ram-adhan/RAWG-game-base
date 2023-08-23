package com.example.rawggamebase

import android.app.Application
import com.example.rawggamebase.data.NetworkClient
import com.example.rawggamebase.data.services.GamesApi
import com.example.rawggamebase.utils.RepositoryModule

class BaseApplication : Application() {
    private lateinit var repositoryModule: RepositoryModule
    val gameRepository get() = repositoryModule.getGamesRepository()
    override fun onCreate() {
        super.onCreate()
        repositoryModule = RepositoryModule(NetworkClient.provideRetrofit())
    }
}