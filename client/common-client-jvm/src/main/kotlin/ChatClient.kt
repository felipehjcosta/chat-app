package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

actual class ChatClient actual constructor(private val url: String) : WebSocketListener() {

    private var websocketClient: WebSocket? = null

    private var receiveMessage: ((Message) -> Unit)? = null

    actual fun start() {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()

        websocketClient = okHttpClient.newWebSocket(request, this)
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    actual fun send(message: Message) {
        websocketClient?.send(JSON.stringify(Message::class.serializer(), message))
    }

    actual fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun onMessage(webSocket: WebSocket, text: String) {
        Logger.info("Message Received: $text")
        receiveMessage?.let { it(JSON.parse(Message::class.serializer(), text)) }
    }
}