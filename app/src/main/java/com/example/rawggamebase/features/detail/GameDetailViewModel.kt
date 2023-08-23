package com.example.rawggamebase.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rawggamebase.BaseApplication
import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.features.main.MainViewModel
import com.example.rawggamebase.utils.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn

class GameDetailViewModel(
    private val gameRepository: GameRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication).gameRepository
                GameDetailViewModel(repository)
            }
        }
    }

    fun getGameDetail(id: Int) = flow<UiState<GameDetailModel>> {
        emit(UiState.Loading)
        gameRepository.getGameDetail(id)
            .flowOn(ioDispatcher)
            .collect {
                when (it) {
                    is Result.Success -> emit(UiState.Success(it.data.toGameDetailModel()))
                    is Result.Error -> emit(UiState.Error(it.error.message ?: "General Error"))
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Init)
}