package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage
import kotlinx.cinterop.StableRef
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze

internal actual open class ChatClient actual constructor(private val url: String) {

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    init {
        ensureNeverFrozen()
    }

    actual open fun start() {
        val chatClientReference = StableRef.create(this)
        val receiveMessageBlock = { webSocketMessage: String ->
            chatClientReference.get().receiveMessage?.invoke(webSocketMessage.toMessage()) ?: Unit
        }.freeze()

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
