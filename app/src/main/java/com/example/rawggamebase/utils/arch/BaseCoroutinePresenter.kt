package com.example.rawggamebase.utils.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutinePresenter<V : BaseView> : CoroutineScope {
    private val supervisorJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = supervisorJob + Dispatchers.Main

    protected var view: V? = null

    fun onAttach(view: V) {
        this.view = view
    }

    fun onDetach() {
        view = null
        supervisorJob.cancelChildren()
    }
}