package com.example.rawggamebase.features.detail

import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.utils.arch.BaseCoroutinePresenter
import com.example.rawggamebase.data.dto.Result
import kotlinx.coroutines.*

class GameDetailPresenter(
    private val gameRepository: GameRepository
) : BaseCoroutinePresenter<GameDetailView>() {

    fun getGameDetail(id: Int) {
        view?.onProgress()
        launch {
            when (val result = gameRepository.getGameDetail(id)) {
                is Result.Success -> view?.onSuccessGetDetail(result.data.toGameDetailModel())
                is Result.Error -> view?.onError(result.error.message.toString())
            }
            view?.onFinishProgress()
        }
    }
}