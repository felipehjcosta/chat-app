package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage
import org.w3c.dom.WebSocket

actual open class WebSocketClient actual constructor(private val url: String) {

    private var socket: WebSocket? = null

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    actual open fun start() {
        socket = WebSocket(url)
        socket?.onmessage = { event ->
            Logger.info("on message event: $event")
            if (event.type == "message") {
                receiveMessage?.invoke(event.asDynamic().data.toString().toMessage())
            } else {
                Logger.info("message event unhandled")
            }
        }
        socket?.onerror = { event ->
            Logger.info("on error event: $event")
            failure?.let { it(Error(event.type)) }
        }
    }

    actual open fun send(message: Message) {
        socket?.send(message.stringify())
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }
}
