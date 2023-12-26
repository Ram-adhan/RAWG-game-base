package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class GameDetail(
    val id: Int,
    val slug: String = "",
    val name: String = "",
    @SerializedName("name_original") val nameOriginal: String = "",
    val description: String? = null,
    val released: String? = null,
    @SerializedName("background_image") val backgroundImage: String? = null,
    @SerializedName("background_image_additional") val backgroundImageAdditional: String? = null,
    val website: String = "",
    val rating: Double = 0.0,
    @SerializedName("rating_top") val ratingTop: Int? = null,
    @SerializedName("added_by_status") val addedByStatus: AddedByStatus? = AddedByStatus(),
    @SerializedName("description_raw") val descriptionRaw: String? = null,
    val developers: List<Developer> = listOf()
)
