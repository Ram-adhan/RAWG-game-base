package com.example.rawggamebase

import android.app.Application
import com.example.data.NetworkClient
import com.example.data.RepositoryModule

class BaseApplication : Application() {
    private lateinit var repositoryModule: RepositoryModule
    val gameRepository get() = repositoryModule.getGamesRepository()
    override fun onCreate() {
        super.onCreate()
        repositoryModule = RepositoryModule(NetworkClient.provideRetrofit())
    }
}