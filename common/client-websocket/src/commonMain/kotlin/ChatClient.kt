package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message

expect open class ChatClient(url: String) {
    open fun start()
    open fun send(message: Message)
    open fun receive(receiveBlock: (Message) -> Unit)
    open fun onFailure(throwableBlock: (Throwable) -> Unit)
}
