package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage

class ChatViewModel internal constructor(private val platformSocket: PlatformSocket) {

    fun start() {
        platformSocket.openSocket(listener = object : PlatformSocketListener {
            override fun onOpen() {}

            override fun onFailure(t: Throwable) {
                showFailureMessage?.invoke(true)
            }

            override fun onMessage(msg: String) {
                onChat?.invoke(msg.toMessage())
            }

            override fun onClosing(code: Int, reason: String) {}

            override fun onClosed(code: Int, reason: String) {}
        })
    }

    fun sendMessage(message: Message) {
        platformSocket.sendMessage(message.stringify())
    }

    var onChat: ((Message) -> Unit)? = null

    var showFailureMessage: ((Boolean) -> Unit)? = null
}
