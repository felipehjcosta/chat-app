package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage
import kotlin.native.concurrent.ensureNeverFrozen

actual open class WebSocketClient actual constructor(private val url: String) {

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    init {
        ensureNeverFrozen()
    }

    actual open fun start() {
        val receiveMessageBlock = { webSocketMessage: String ->
            receiveMessage?.invoke(webSocketMessage.toMessage()) ?: Unit
        }

        urlSessionWebSocketTaskWrapper.start(url, receiveMessageBlock)
    }

    actual open fun send(message: Message) {
        urlSessionWebSocketTaskWrapper.sendMessage(message.stringify())
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

}
