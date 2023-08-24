package com.example.rawggamebase.features.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rawggamebase.databinding.ActivityMainBinding
import com.example.rawggamebase.features.adapters.GameListAdapter
import com.example.rawggamebase.features.detail.GameDetailActivity
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl
import com.example.rawggamebase.utils.UiState
import kotlinx.coroutines.launch

class GameListActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    companion object {
        fun newIntent(context: Context) = Intent(context, GameListActivity::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ListViewModel by viewModels { ListViewModel.Factory }
    private val gameAdapter: GameListAdapter by lazy { GameListAdapter(this::onItemClick) }
    private var readyToLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "RAWG Game Database"

        observeData()
        initView()
        initializeLoadingDialog(this)
        viewModel.searchGames("")
    }

    private fun initView() {
        binding.rvGames.apply {
            adapter = gameAdapter
            val llManager = LinearLayoutManager(this@GameListActivity)
            layoutManager = llManager
            var visibleItemCount = 0
            var totalItemCount = 0
            var pastItemVisible = 0
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    visibleItemCount = llManager.childCount
                    totalItemCount = llManager.itemCount
                    pastItemVisible = llManager.findFirstVisibleItemPosition()
                    if (readyToLoad) {
                        if ((visibleItemCount + pastItemVisible) >= totalItemCount) {
                            readyToLoad = false
                            viewModel.onScrollPage()
                        }
                    }
                }
            })
        }

        binding.etSearch.doAfterTextChanged {
            viewModel.searchGames(it.toString())
        }
    }

    private fun onItemClick(gameModel: GameModel) {
        startActivity(GameDetailActivity.newIntent(this, gameModel.id))
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.gameList
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    setProgressVisibility(state is UiState.Loading)
                    readyToLoad = state !is UiState.Loading
                    when (state) {
                        is UiState.Success -> {
                            gameAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            Toast.makeText(this@GameListActivity, state.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            /*do nothing*/
                        }
                    }
                }
        }
    }
}