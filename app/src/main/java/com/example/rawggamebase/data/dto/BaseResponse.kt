package com.example.rawggamebase.data.dto

data class BaseResponse<out T : Any>(
    val count: Int = 0,
    val next: String = "",
    val previous: String = "",
    val results: T
)
