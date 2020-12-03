package com.github.felipehjcosta.chatapp.client

object EmptyPlatformSocketListener : PlatformSocketListener {

    override fun onOpen() {}

    override fun onFailure(t: Throwable) {}

    override fun onMessage(msg: String) {}

    override fun onClosing(code: Int, reason: String) {}

    override fun onClosed(code: Int, reason: String) {}
}