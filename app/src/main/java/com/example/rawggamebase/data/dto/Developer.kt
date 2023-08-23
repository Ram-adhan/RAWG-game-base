package com.example.rawggamebase.data.dto

import com.google.gson.annotations.SerializedName

data class Developer(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("games_count") val gamesCount: Int? = null,
    @SerializedName("image_background") val imageBackground: String? = null

)
