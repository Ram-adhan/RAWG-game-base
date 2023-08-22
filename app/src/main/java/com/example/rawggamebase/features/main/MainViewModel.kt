package com.example.rawggamebase.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainViewModel : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel()
            }
        }
    }

    private var _gameList: MutableSharedFlow<List<GameModel>> = MutableSharedFlow(replay = 1)
    val gameList = _gameList.asSharedFlow()

    fun getGameList() {
        val gameList = listOf(
            GameModel(
                id = 1,
                "Fast and Furi",
                "12",
                "12",
                ""
            )
        )

        _gameList.tryEmit(gameList)
    }
}