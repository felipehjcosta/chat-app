package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

actual class ChatClient actual constructor(url: String) : WebSocketListener() {

    private val websocketClient: WebSocket

    private var receiveMessage: ((Message) -> Unit)? = null

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()

        websocketClient = okHttpClient.newWebSocket(request, this)
    }

    @ImplicitReflectionSerializer
    actual fun send(message: Message) {
        websocketClient.send(JSON.stringify(Message::class.serializer(), message))
    }

    actual fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    @ImplicitReflectionSerializer
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        receiveMessage?.let { it(JSON.parse(Message::class.serializer(), text)) }
    }
}