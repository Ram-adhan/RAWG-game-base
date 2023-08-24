package com.example.rawggamebase.features.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rawggamebase.databinding.ActivityMainBinding
import com.example.rawggamebase.features.adapters.GameListAdapter
import com.example.rawggamebase.features.detail.GameDetailActivity
import com.example.rawggamebase.features.list.GameListActivity
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.UiState
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val gameListAdapter: GameListAdapter by lazy { GameListAdapter(onItemClick = this::onItemClick) }
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
            adapter = gameListAdapter.apply {
                onViewMore = this@MainActivity::onViewMoreClick
            }
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.etSearch.isVisible = false

        initializeLoadingDialog(this)
    }

    private fun onItemClick(gameModel: GameModel) {
        startActivity(GameDetailActivity.newIntent(this, gameModel.id))
    }

    private fun onViewMoreClick() {
        startActivity(GameListActivity.newIntent(this))
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