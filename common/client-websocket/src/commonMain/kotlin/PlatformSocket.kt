package com.github.felipehjcosta.chatapp.client

interface PlatformSocketListener {
    fun onOpen()
    fun onFailure(t: Throwable)
    fun onMessage(msg: String)
    fun onClosing(code: Int, reason: String)
    fun onClosed(code: Int, reason: String)
}

expect open class PlatformSocket(url: String) {
    open fun openSocket(listener: PlatformSocketListener)
    open fun closeSocket(code: Int, reason: String)
    open fun sendMessage(msg: String)
}