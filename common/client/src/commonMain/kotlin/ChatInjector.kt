package com.github.felipehjcosta.chatapp.client

object ChatInjector {

    private var baseUrl: String = ""

    operator fun invoke(block: ChatInjectorConfiguration.() -> Unit) {
        this@ChatInjector.baseUrl = ChatInjectorConfiguration().apply(block).baseUrl
    }

    class ChatInjectorConfiguration {
        var baseUrl: String = ""
    }

    fun viewModel(): Lazy<ChatViewModel> = lazy {
        ChatViewModel(ChatClient(ChatInjector.baseUrl))
    }

}