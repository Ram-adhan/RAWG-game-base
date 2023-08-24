package com.example.rawggamebase.features.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rawggamebase.databinding.ActivityMainBinding
import com.example.rawggamebase.features.adapters.GameListAdapter
import com.example.rawggamebase.features.detail.GameDetailActivity
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl

class GameListActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    companion object {
        fun newIntent(context: Context) = Intent(context, GameListActivity::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ListViewModel by viewModels { ListViewModel.Factory }
    private val gameAdapter: GameListAdapter by lazy { GameListAdapter(this::onItemClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "RAWG Game Database"

        initView()
    }

    private fun initView() {
        binding.rvGames.apply {
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(this@GameListActivity)
        }

        binding.etSearch.doAfterTextChanged {
            viewModel.searchGames(it.toString())
        }
    }

    private fun onItemClick(gameModel: GameModel) {
        startActivity(GameDetailActivity.newIntent(this, gameModel.id))
    }
}