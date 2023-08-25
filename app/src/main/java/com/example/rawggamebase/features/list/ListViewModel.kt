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
import com.example.rawggamebase.features.model.toGameModel
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

    private var cachedGame = mutableListOf<GameModel>()
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
    private var _currentPage: Int = 1
    val currentPage get() = _currentPage
    private var canGoNext: Boolean = true
    private var searchJob: Job? = null

    fun searchGames(keyword: String) {
        searchJob?.cancel()
        searchJob = null

        currentKeyword = keyword

        _currentPage = 1
        searchJob = viewModelScope.launch(ioDispatcher) {
            delay(500)
            getGames(currentKeyword, _currentPage)
        }

        searchJob?.start()
    }

    fun onScrollPage() {
        if (_gameList.value is UiState.Loading || !canGoNext) return
        _currentPage++
        viewModelScope.launch(ioDispatcher) {
            _gameList.emit(UiState.Loading)
            getGames(currentKeyword, _currentPage)
        }
    }

    private suspend fun getGames(keyword: String, page: Int? = null) {
        val searchKeyword = keyword.ifBlank { null }
        when (val result = gameRepository.getGames(searchKeyword, page)) {
            is Result.Success -> {
                canGoNext = result.isNextPageAvailable
                if (page == null || page < 2) {
                    cachedGame.clear()
                    _gameList.emit(
                        UiState.Success(cachedGame.toList())
                    )
                }
                val newData = result.data.map { it.toGameModel() }
                cachedGame.addAll(newData)
                _gameList.emit(
                    UiState.Success(cachedGame.map { it.copy() }.toList())
                )
            }
            is Result.Error -> {
                _currentPage--
                if (_currentPage < 1)
                    _currentPage = 1
                _gameList.emit(UiState.Error(result.error.message ?: ""))
            }
        }
    }

}