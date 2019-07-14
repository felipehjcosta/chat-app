package com.github.felipehjcosta.chatapp.client

import cocoapods.SocketRocket.SRWebSocket
import cocoapods.SocketRocket.SRWebSocketDelegateProtocol
import com.github.felipehjcosta.chatapp.Message
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import platform.Foundation.NSURL
import platform.darwin.NSObject

@UseExperimental(ImplicitReflectionSerializer::class)
internal actual open class ChatClient actual constructor(private val url: String) {

    private var socket: SRWebSocket? = null

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    private val webSocketDelegate = WebSocketDelegate()

    actual open fun start() {
        socket = SRWebSocket(NSURL(string = url)).apply {
            delegate = webSocketDelegate
        }.also {
            it.open()
        }
    }

    actual open fun send(message: Message) {
        socket?.send(Json.stringify(Message.serializer(), message))
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

    inner class WebSocketDelegate : NSObject(), SRWebSocketDelegateProtocol {
        override fun webSocket(webSocket: SRWebSocket?, didReceiveMessage: Any?) {
            receiveMessage?.invoke(Json.parse(Message.serializer(), didReceiveMessage.toString()))
        }
    }
}
