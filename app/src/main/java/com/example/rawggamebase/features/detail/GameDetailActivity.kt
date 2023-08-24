package com.example.rawggamebase.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rawggamebase.R
import com.example.rawggamebase.databinding.ActivityDetailBinding
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl
import com.example.rawggamebase.utils.UiState
import com.example.rawggamebase.utils.isPositiveNumber
import kotlinx.coroutines.launch

class GameDetailActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    companion object {
        private const val ID_DATA = "idData"
        fun newIntent(context: Context, id: Int) =
            Intent(context, GameDetailActivity::class.java).apply {
                putExtra(ID_DATA, id)
            }
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: GameDetailViewModel by viewModels { GameDetailViewModel.Factory }
    private val id: Int by lazy { intent.getIntExtra(ID_DATA, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLoadingDialog(this)
        collectData()
    }

    private fun collectData() {
        if (!id.isPositiveNumber) return

        lifecycleScope.launch {

            viewModel.getGameDetail(id)
                .flowWithLifecycle(lifecycle)
                .collect {
                    setProgressVisibility(it == UiState.Loading)
                    when (it) {
                        is UiState.Success -> bindData(it.data)
                        is UiState.Error -> Toast.makeText(
                            this@GameDetailActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> {
                            /*do nothing*/
                        }
                    }
                }
        }
    }

    private fun bindData(data: GameDetailModel) {
        Glide.with(this)
            .load(data.coverImage)
            .into(binding.ivCoverImage)

        with(binding) {
            tvGameStudio.text = data.developer
            tvTitle.text = data.title
            tvRating.text = data.rating
            tvTotalPlayed.text = getString(R.string.player_count, data.totalPlayed)
            tvReleaseDate.text = getString(R.string.release_date_template, data.releaseDate)
            tvDescription.text = data.description
        }
    }
}