package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message

class ChatViewModel(private val chatClient: ChatClient) {

    init {
        chatClient.onFailure {
            showFailureMessage?.invoke(true)
        }
        chatClient.receive {
            onChat?.invoke(it)
        }
    }

    fun start() {
        chatClient.start()
    }

    fun sendMessage(message: Message) {
        chatClient.send(message)
    }

    var onChat: ((Message) -> Unit)? = null

    var showFailureMessage: ((Boolean) -> Unit)? = null
}