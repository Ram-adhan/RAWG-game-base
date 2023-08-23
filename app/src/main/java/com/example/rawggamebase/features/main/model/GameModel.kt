package com.example.rawggamebase.features.main.model

import com.example.rawggamebase.data.dto.Game

data class GameModel(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val rating: String,
    val imageLink: String,
)

fun Game.toGameModel() = GameModel(
    id = this.id,
    title = this.name,
    releaseDate = this.released.toString(),
    rating = this.rating.toString(),
    imageLink = this.backgroundImage
)
