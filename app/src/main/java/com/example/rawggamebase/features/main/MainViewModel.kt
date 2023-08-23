package com.example.rawggamebase.features.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rawggamebase.BaseApplication
import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.features.main.model.GameModel
import com.example.rawggamebase.features.main.model.UiState
import com.example.rawggamebase.features.main.model.toGameModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val gameRepository: GameRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as BaseApplication).gameRepository
                MainViewModel(repository)
            }
        }
    }

    private var _gameList: MutableStateFlow<UiState<List<GameModel>>> =
        MutableStateFlow(UiState.Init)
    val gameList: StateFlow<UiState<List<GameModel>>> =
        _gameList
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Init
            )

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _gameList.value = UiState.Loading
            getGames("")
        }
    }

    fun searchGames(keyword: String) {
        searchJob?.cancel()
        searchJob = null

        searchJob = viewModelScope.launch {
            delay(500)
            getGames(keyword)
        }

        searchJob?.start()
    }

    private suspend fun getGames(keyword: String) {
        gameRepository.getGames(keyword)
            .flowOn(ioDispatcher)
            .collectLatest {
                when (it) {
                    is Result.Success -> {
                        _gameList.emit(UiState.Success(it.data.map { game -> game.toGameModel() }))
                    }

                    is Result.Error -> {
                        _gameList.emit(UiState.Error(it.error.message ?: "General Error"))
                    }
                }
            }
    }
}