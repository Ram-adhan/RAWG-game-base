package com.example.rawggamebase.features.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rawggamebase.databinding.ActivityMainBinding
import com.example.rawggamebase.features.main.model.UiState
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val gameListAdapter: GameListAdapter by lazy { GameListAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observerGameList()
        initView()
    }

    private fun initView() {
        supportActionBar?.title = "Games For You"

        binding.rvGames.apply {
            adapter = gameListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        initializeLoadingDialog(this)

    }

    private fun observerGameList() {
        lifecycleScope.launch {
            viewModel.gameList
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    stackProgress(state is UiState.Loading)
                    when (state) {
                        is UiState.Loading -> showProgress()
                        is UiState.Error -> {
                            Toast
                                .makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UiState.Success -> {
                            gameListAdapter.submitList(state.data)
                        }

                        else -> {
                            /*do nothing*/
                        }
                    }
                }
        }
    }
}