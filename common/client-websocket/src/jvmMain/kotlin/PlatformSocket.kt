package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket

actual open class PlatformSocket actual constructor(url: String) {

    private val socketEndpoint = url
    private var webSocket: WebSocket? = null

    actual open fun openSocket(listener: PlatformSocketListener) {
        val socketRequest = Request.Builder().url(socketEndpoint).build()
        val webClient = OkHttpClient().newBuilder().build()
        webSocket = webClient.newWebSocket(
            socketRequest,
            object : okhttp3.WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Logger.info("WebSocketListener::onOpen with response: ${response}")
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onOpen()
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Logger.info("WebSocketListener::onFailure with Throwable: ${t} and response: ${response}")
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onFailure(t)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Logger.info("WebSocketListener::onMessage with text: ${text}")
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onMessage(text)
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Logger.info("WebSocketListener::onClosing with code: ${code} and reason: ${reason}")
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onClosing(code, reason)
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Logger.info("WebSocketListener::onClosed with code: ${code} and reason: ${reason}")
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onClosed(code, reason)
                    }
                }
            }
        )
    }

    actual open fun closeSocket(code: Int, reason: String) {
        webSocket?.close(code, reason)
        webSocket = null
    }

    actual open fun sendMessage(msg: String) {
        webSocket?.send(msg)
    }
}