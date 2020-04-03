package com.github.felipehjcosta.chatapp

import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scheduleScrollToEnd() {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            scrollToEnd()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun RecyclerView.scrollToEnd() = (adapter?.itemCount ?: 0 - 1).takeIf { it > 0 }?.let(this::smoothScrollToPosition)
