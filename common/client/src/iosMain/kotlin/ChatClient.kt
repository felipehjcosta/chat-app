package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import kotlinx.serialization.ImplicitReflectionSerializer

@UseExperimental(ImplicitReflectionSerializer::class)
internal actual open class ChatClient actual constructor(private val url: String) {

    actual open fun start() {
        TODO()
    }

    actual open fun send(message: Message) {
        TODO()
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        TODO()
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        TODO()
    }
}
