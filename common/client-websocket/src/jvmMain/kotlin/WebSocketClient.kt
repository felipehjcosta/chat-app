package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

actual open class WebSocketClient actual constructor(private val url: String) {

    private var websocketClient: WebSocket? = null

    private var receiveMessage: ((String) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    actual open fun start() {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()

        websocketClient = okHttpClient.newWebSocket(request, ChatClientWebSocketListener())
    }

    actual open fun send(webSocketMessage: String) {
        websocketClient?.send(webSocketMessage)
    }

    actual open fun receive(receiveBlock: (String) -> Unit) {
        receiveMessage = receiveBlock
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

    inner class ChatClientWebSocketListener : WebSocketListener() {

        override fun onMessage(webSocket: WebSocket, text: String) {
            GlobalScope.launch(Dispatchers.Main) {
                Logger.info("String Received: $text")
                receiveMessage?.invoke(text)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            GlobalScope.launch(Dispatchers.Main) {
                Logger.info("WebSocket Failure: $t")
                failure?.invoke(t)
            }
        }
    }
}
