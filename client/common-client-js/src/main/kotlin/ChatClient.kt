package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.io.IOException
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import org.w3c.dom.WebSocket

@UseExperimental(ImplicitReflectionSerializer::class)
internal actual open class ChatClient actual constructor(private val url: String) {

    private var socket: WebSocket? = null

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    actual open fun start() {
        socket = WebSocket(url)
        socket?.onmessage = { event ->
            Logger.info("on message event: $event")
            if (event.type == "message") {
                receiveMessage?.invoke(JSON.parse(Message::class.serializer(), event.asDynamic().data.toString()))
            } else {
                Logger.info("message event unhandled")
            }
        }
        socket?.onerror = { event ->
            Logger.info("on error event: $event")
            failure?.let { it(IOException(event.type)) }
        }
    }

    actual open fun send(message: Message) {
        socket?.send(JSON.stringify(message))
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }
}
