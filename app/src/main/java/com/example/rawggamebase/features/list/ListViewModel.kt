package com.example.rawggamebase.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rawggamebase.BaseApplication
import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val gameRepository: GameRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as BaseApplication).gameRepository
                ListViewModel(repository)
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

    private var currentKeyword: String = ""

    private var searchJob: Job? = null


    fun searchGames(keyword: String) {
        searchJob?.cancel()
        searchJob = null

        currentKeyword = keyword

        searchJob = viewModelScope.launch {
            delay(500)
            getGames(currentKeyword)
        }

        searchJob?.start()
    }

    private suspend fun getGames(keyword: String, page: Int? = null) {
        val searchKeyword = keyword.ifBlank { null }
        /*when(val result = gameRepository.getGames(searchKeyword, page)) {
            is Result.Success ->
        }
            .collect {
                when (it) {

                }
            }*/
    }

}