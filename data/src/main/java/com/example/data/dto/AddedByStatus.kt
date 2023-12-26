package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class AddedByStatus(
    val yet: Int? = null,
    val owned: Int? = null,
    val beaten: Int? = null,
    val toplay: Int? = null,
    val dropped: Int? = null,
    val playing: Int? = null
)
