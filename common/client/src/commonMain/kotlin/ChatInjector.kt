package com.github.felipehjcosta.chatapp.client

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ChatInjector {

    var baseUrl: String = ""

    operator fun invoke(block: ChatInjectorConfiguration.() -> Unit) {
        this@ChatInjector.baseUrl = ChatInjectorConfiguration().apply(block).baseUrl
    }

    class ChatInjectorConfiguration {
        var baseUrl: String = ""
    }

    fun createViewModel(): ChatViewModel = ChatViewModel(ChatClient(ChatInjector.baseUrl))

    fun viewModel(): Lazy<ChatViewModel> = lazy {
        createViewModel()
    }

}
