package com.example.rawggamebase.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rawggamebase.BuildConfig
import com.example.rawggamebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}