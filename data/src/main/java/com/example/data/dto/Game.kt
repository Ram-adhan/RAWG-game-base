package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class Game(
    val added: Int = 0,
    @SerializedName("background_image")
    val backgroundImage: String? = "",
    val id: Int = 0,
    val metacritic: Int = 0,
    val name: String = "",
    val playtime: Int = 0,
    val rating: Double = 0.0,
    val released: String? = "",
)