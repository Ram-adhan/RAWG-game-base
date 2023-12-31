package com.example.rawggamebase.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rawggamebase.BaseApplication
import com.example.data.GameRepository
import com.example.data.dto.Result
import com.example.rawggamebase.utils.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class GameDetailViewModel(
    private val gameRepository: com.example.data.GameRepository,
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
        when (val result = gameRepository.getGameDetail(id)) {
            is com.example.data.dto.Result.Success -> emit(UiState.Success(result.data.toGameDetailModel()))
            is com.example.data.dto.Result.Error -> emit(
                UiState.Error(
                    result.error.message ?: "General Error"
                )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Init)
}