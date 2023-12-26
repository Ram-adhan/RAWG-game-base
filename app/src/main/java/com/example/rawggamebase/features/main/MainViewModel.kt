package com.example.rawggamebase.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rawggamebase.BaseApplication
import com.example.data.GameRepository
import com.example.data.dto.Result
import com.example.rawggamebase.features.adapters.GameListAdapter
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.features.model.toGameModel
import com.example.rawggamebase.utils.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val gameRepository: com.example.data.GameRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as BaseApplication).gameRepository
                MainViewModel(repository)
            }
        }
    }

    val gameList: StateFlow<UiState<List<GameModel>>> by lazy {
        flow<UiState<List<GameModel>>> {
            emit(UiState.Loading)
            when (val result = gameRepository.getGames(searchKey = null, page = null)) {
                is com.example.data.dto.Result.Success -> {
                    val data = result.data
                        .map { game -> game.toGameModel() }
                        .toMutableList().apply {
                            add(GameModel(viewType = GameListAdapter.VIEW_MORE))
                        }

                    emit(UiState.Success(data))
                }

                is com.example.data.dto.Result.Error -> {
                    emit(UiState.Error(result.error.message ?: "General Error"))
                }
            }
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Init
            )
    }
}