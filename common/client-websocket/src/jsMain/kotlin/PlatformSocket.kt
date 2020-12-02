package com.github.felipehjcosta.chatapp.client

import org.w3c.dom.WebSocket
import com.github.felipehjcosta.chatapp.logging.Logger

actual open class PlatformSocket actual constructor(url: String) {

    private val socketEndpoint = url

    private var socket: WebSocket? = null

    actual open fun openSocket(listener: PlatformSocketListener) {
        socket = WebSocket(socketEndpoint)
        socket?.onmessage = { event ->
            Logger.info("on String event: $event")
            if (event.type == "message") {
                listener.onMessage(event.asDynamic().data.toString())
            } else {
                Logger.info("message event unhandled")
            }
        }
        socket?.onerror = { event ->
            Logger.info("on error event: $event")
            listener.onFailure(Error(event.type))
        }
    }

    actual open fun closeSocket(code: Int, reason: String) {
        socket?.close(code.toShort(), reason)
        socket = null
    }

    actual open fun sendMessage(msg: String) {
        socket?.send(msg)
    }
}