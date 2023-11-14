package com.example.rawggamebase.features.detail

import com.example.rawggamebase.utils.arch.BaseView

interface GameDetailView : BaseView {
    fun onSuccessGetDetail(detail: GameDetailModel)
    fun onError(message: String)
}