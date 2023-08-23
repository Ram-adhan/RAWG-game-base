package com.example.rawggamebase.data.dto

import com.google.gson.annotations.SerializedName

data class Game(
    val added: Int = 0,
    @SerializedName("background_image")
    val backgroundImage: String = "",
    val id: Int = 0,
    val metacritic: Int = 0,
    val name: String = "",
    val playtime: Int = 0,
    val rating: Double = 0.0,
    @SerializedName("rating_top")
    val ratingTop: Double = 0.0,
    @SerializedName("rating_count")
    val ratingsCount: Int = 0,
    val released: String = "",
    @SerializedName("reviews_text_count")
    val reviewsTextCount: String = "",
    val slug: String = "",
    @SerializedName("suggestions_count")
    val suggestionsCount: Int = 0,
    val tba: Boolean = false,
    val updated: String = ""
)