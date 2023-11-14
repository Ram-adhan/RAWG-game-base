package com.example.rawggamebase.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rawggamebase.BaseApplication
import com.example.rawggamebase.R
import com.example.rawggamebase.databinding.ActivityDetailBinding
import com.example.rawggamebase.databinding.CustomActionBarBinding
import com.example.rawggamebase.utils.LoadingHandler
import com.example.rawggamebase.utils.LoadingHandlerImpl
import com.example.rawggamebase.utils.UiState
import com.example.rawggamebase.utils.isPositiveNumber
import com.example.rawggamebase.utils.setCustomToolbar
import kotlinx.coroutines.launch

class GameDetailActivity : AppCompatActivity(), GameDetailView,
    LoadingHandler by LoadingHandlerImpl() {
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
    private val presenter: GameDetailPresenter by lazy { GameDetailPresenter((application as BaseApplication).gameRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLoadingDialog(this)
//        collectData()
        setCustomToolbar(binding.customToolbar, showNavigateUp = true, showAction = true)
        presenter.onAttach(this)
        presenter.getGameDetail(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
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

        binding.customToolbar.title.text = data.title
        with(binding) {
            tvGameStudio.text = data.developer
            tvTitle.text = data.title
            tvRating.text = data.rating
            tvTotalPlayed.text = getString(R.string.player_count, data.totalPlayed)
            tvReleaseDate.text = getString(R.string.release_date_template, data.releaseDate)
            tvDescription.text = data.description
        }
    }

    override fun onProgress() {
        stackProgress(true)
    }

    override fun onFinishProgress() {
        stackProgress(false)
    }

    override fun onSuccessGetDetail(detail: GameDetailModel) {
        bindData(detail)
    }

    override fun onError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}