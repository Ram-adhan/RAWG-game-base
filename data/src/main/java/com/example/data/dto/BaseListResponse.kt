package com.example.data.dto

data class BaseListResponse<out T : Any>(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: T
)
