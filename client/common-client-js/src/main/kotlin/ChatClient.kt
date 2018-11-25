package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.WebSocket

@UseExperimental(ImplicitReflectionSerializer::class)
actual class ChatClient actual constructor(url: String) {

    private val socket: WebSocket = WebSocket(url)

    private var receiveMessage: ((Message) -> Unit)? = null

    init {
        socket.onmessage = { event ->
            Logger.info("on message event: $event")
            if (event.type == "message") {
                receiveMessage?.let {
                    it(kotlinx.serialization.json.JSON.parse(Message::class.serializer(), event.asDynamic().data.toString()))
                }
            } else {
                Logger.info("message event unhandled")
            }
        }
    }

    actual fun send(message: Message) {
        socket.send(JSON.stringify(message))
    }

    actual fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }
}