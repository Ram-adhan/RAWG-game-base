package com.example.rawggamebase.features.detail

import com.example.data.GameRepository
import com.example.rawggamebase.utils.arch.BaseCoroutinePresenter
import com.example.data.dto.Result
import kotlinx.coroutines.*

class GameDetailPresenter(
    private val gameRepository: com.example.data.GameRepository
) : BaseCoroutinePresenter<GameDetailView>() {

    fun getGameDetail(id: Int) {
        view?.onProgress()
        launch {
            when (val result = gameRepository.getGameDetail(id)) {
                is com.example.data.dto.Result.Success -> view?.onSuccessGetDetail(result.data.toGameDetailModel())
                is com.example.data.dto.Result.Error -> view?.onError(result.error.message.toString())
            }
            view?.onFinishProgress()
        }
    }
}