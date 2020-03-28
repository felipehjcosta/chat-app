package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal actual open class ChatClient actual constructor(private val url: String) : WebSocketListener() {

    private var websocketClient: WebSocket? = null

    private var receiveMessage: ((Message) -> Unit)? = null

    private var failure: ((Throwable) -> Unit)? = null

    actual open fun start() {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()

        websocketClient = okHttpClient.newWebSocket(request, this)
    }

    actual open fun send(message: Message) {
        websocketClient?.send(message.stringify())
    }

    actual open fun receive(receiveBlock: (Message) -> Unit) {
        receiveMessage = receiveBlock
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Logger.info("Message Received: $text")
            receiveMessage?.invoke(text.toMessage())
        }
    }

    actual open fun onFailure(throwableBlock: (Throwable) -> Unit) {
        failure = throwableBlock
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch(Dispatchers.Main) {
            Logger.info("WebSocket Failure: $t")
            failure?.invoke(t)
        }
    }
}
