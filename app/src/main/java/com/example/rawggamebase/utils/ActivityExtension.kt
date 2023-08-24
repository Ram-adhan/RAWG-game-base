package com.example.rawggamebase.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.rawggamebase.R
import com.example.rawggamebase.databinding.CustomActionBarBinding

fun AppCompatActivity.setCustomToolbar(
    binding: CustomActionBarBinding,
    showNavigateUp: Boolean,
    showAction: Boolean
) {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.title = ""
    if (showNavigateUp) {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_24)
        supportActionBar?.setHomeButtonEnabled(false)
    }

    binding.actionIcon.isVisible = showAction
}