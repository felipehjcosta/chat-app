package com.github.felipehjcosta.chatapp.client

import kotlin.native.concurrent.ensureNeverFrozen

actual open class WebSocketClient actual constructor(private val url: String) {

    private var receiveMessage: ((String) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    init {
        ensureNeverFrozen()
    }

    actual open fun start() {
        val receiveMessageBlock = { webSocketMessage: String ->
            receiveMessage?.invoke(webSocketMessage) ?: Unit
        }

        urlSessionWebSocketTaskWrapper.start(url, receiveMessageBlock)
    }

    actual open fun send(webSocketMessage: String) {
        urlSessionWebSocketTaskWrapper.sendMessage(webSocketMessage)
    }

    actual open fun receive(receiveBlock: (String) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

}
