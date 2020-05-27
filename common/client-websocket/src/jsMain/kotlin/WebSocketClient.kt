package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.logging.Logger
import org.w3c.dom.WebSocket

actual open class WebSocketClient actual constructor(private val url: String) {

    private var socket: WebSocket? = null

    private var receiveMessage: ((String) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    actual open fun start() {
        socket = WebSocket(url)
        socket?.onmessage = { event ->
            Logger.info("on String event: $event")
            if (event.type == "message") {
                receiveMessage?.invoke(event.asDynamic().data.toString())
            } else {
                Logger.info("message event unhandled")
            }
        }
        socket?.onerror = { event ->
            Logger.info("on error event: $event")
            failure?.let { it(Error(event.type)) }
        }
    }

    actual open fun send(webSocketMessage: String) {
        socket?.send(webSocketMessage)
    }

    actual open fun receive(receiveBlock: (String) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }
}
