package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import kotlinx.cinterop.StableRef
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze

@UseExperimental(ImplicitReflectionSerializer::class)
internal actual open class ChatClient actual constructor(private val url: String) {

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    init {
        ensureNeverFrozen()
    }

    actual open fun start() {
        val chatClientReference = StableRef.create(this)
        val receiveMessageBlock = { webSocketMessage: String ->
            chatClientReference.get().receiveMessage?.invoke(webSocketMessage.convertToMessage()) ?: Unit
        }.freeze()

        urlSessionWebSocketTaskWrapper.start(url, receiveMessageBlock)
    }

    actual open fun send(message: Message) {
        urlSessionWebSocketTaskWrapper.sendMessage(message.convertToString())
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

}

private fun String.convertToMessage() = Json.parse(Message.serializer(), this)

private fun Message.convertToString() = Json.stringify(Message.serializer(), this)
