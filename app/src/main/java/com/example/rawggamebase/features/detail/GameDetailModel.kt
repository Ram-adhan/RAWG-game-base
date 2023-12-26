package com.example.rawggamebase.features.detail

import com.example.data.dto.GameDetail
import com.example.rawggamebase.utils.filterInt

data class GameDetailModel(
    val id: Int,
    val title: String,
    val coverImage: String,
    val developer: String,
    val rating: String,
    val releaseDate: String,
    val totalPlayed: String,
    val description: String,
)

fun GameDetail.toGameDetailModel() = GameDetailModel(
    id = this.id,
    title = this.name,
    coverImage = this.backgroundImage ?: "",
    developer = this.developers.firstOrNull()?.name ?: "",
    rating = this.rating.toString(),
    releaseDate = this.released ?: "",
    totalPlayed = listOf(
        this.addedByStatus?.playing.filterInt,
        this.addedByStatus?.beaten.filterInt,
        this.addedByStatus?.toplay.filterInt,
        this.addedByStatus?.dropped.filterInt,
    ).sumOf { it }.toString(),
    description = this.descriptionRaw ?: ""
)
