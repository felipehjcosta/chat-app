package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message

expect class ChatClient(url: String) {
    fun start()
    fun send(message: Message)
    fun receive(receiveBlock: (Message) -> Unit)
    fun onFailure(throwableBlock: (Throwable) -> Unit)
}