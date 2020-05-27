package com.github.felipehjcosta.chatapp.client

import com.github.felipehjcosta.chatapp.Message
import com.github.felipehjcosta.chatapp.stringify
import com.github.felipehjcosta.chatapp.toMessage

class ChatViewModel internal constructor(private val chatClient: WebSocketClient) {

    init {
        chatClient.onFailure {
            showFailureMessage?.invoke(true)
        }
        chatClient.receive {
            onChat?.invoke(it.toMessage())
        }
    }

    fun start() {
        chatClient.start()
    }

    fun sendMessage(message: Message) {
        chatClient.send(message.stringify())
    }

    var onChat: ((Message) -> Unit)? = null

    var showFailureMessage: ((Boolean) -> Unit)? = null
}
