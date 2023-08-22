package com.example.rawggamebase.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.rawggamebase.R

interface LoadingHandler {
    fun initializeLoadingDialog(context: AppCompatActivity)
    fun showProgress()
    fun dismissProgress()
    fun setProgressVisibility(isVisible: Boolean)
}

class LoadingHandlerImpl : LoadingHandler, LifecycleEventObserver {
    private var loadingDialog: Dialog? = null

    override fun initializeLoadingDialog(context: AppCompatActivity) {
        loadingDialog = Dialog(context)
        loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog?.setContentView(R.layout.loading_layout)
        loadingDialog?.setCancelable(false)

        context.lifecycle.addObserver(this)
    }

    override fun showProgress() {
        loadingDialog?.show()
    }

    override fun dismissProgress() {
        loadingDialog?.dismiss()
    }

    override fun setProgressVisibility(isVisible: Boolean) {
        if (isVisible) {
            showProgress()
        } else {
            dismissProgress()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> loadingDialog?.dismiss()
            else -> {}
        }
    }
}