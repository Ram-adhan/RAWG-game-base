package com.example.rawggamebase.features.model

import com.example.data.dto.Game

data class GameModel(
    val id: Int = -1,
    val title: String = "",
    val releaseDate: String = "",
    val rating: String = "",
    val imageLink: String = "",
    val viewType: Int = 0,
)

fun com.example.data.dto.Game.toGameModel() = GameModel(
    id = this.id,
    title = this.name,
    releaseDate = this.released.toString(),
    rating = this.rating.toString(),
    imageLink = this.backgroundImage ?: ""
)
