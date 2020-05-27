package com.github.felipehjcosta.chatapp.client

expect open class WebSocketClient(url: String) {
    open fun start()
    open fun send(webSocketMessage: String)
    open fun receive(receiveBlock: (String) -> Unit)
    open fun onFailure(throwableBlock: (Throwable) -> Unit)
}
