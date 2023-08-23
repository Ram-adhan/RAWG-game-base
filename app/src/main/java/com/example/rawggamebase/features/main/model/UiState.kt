package com.example.rawggamebase.features.main.model

sealed interface UiState<out T : Any> {
    object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Success<out T : Any>(val data: T) : UiState<T>
    object Init : UiState<Nothing>
}