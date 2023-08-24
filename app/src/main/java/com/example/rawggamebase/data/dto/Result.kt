package com.example.rawggamebase.data.dto

sealed interface Result<out T> {
    class Success<out T : Any>(val data: T, val isNextPageAvailable: Boolean = false) : Result<T>
    class Error(val error: Throwable) : Result<Nothing>
}