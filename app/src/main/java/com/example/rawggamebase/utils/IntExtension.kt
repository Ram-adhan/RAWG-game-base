package com.example.rawggamebase.utils

val Int.isPositiveNumber get() = this >= 0
val Int?.filterInt: Int get() = this ?: 0