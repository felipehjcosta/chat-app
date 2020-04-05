package com.github.felipehjcosta.chatapp.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionWebSocketMessage
import platform.Foundation.NSURLSessionWebSocketTask
import kotlin.native.concurrent.freeze

@ThreadLocal
val urlSessionWebSocketTaskWrapper: URLSessionWebSocketTaskWrapper = URLSessionWebSocketTaskWrapper()

class URLSessionWebSocketTaskWrapper {

    private lateinit var webSocketTask: NSURLSessionWebSocketTask

    private val webSocketChannel = Channel<String>()

    fun start(url: String, receiveMessageBlock: (String) -> Unit) {
        prepareWebSocketConsumer(receiveMessageBlock)
        webSocketTask = createWebSocketTask(url)
        prepareToReceiveIncomingMessage()
        webSocketTask.resume()
    }

    private fun prepareWebSocketConsumer(receiveMessageBlock: (String) -> Unit) {
        consumeWebSocketMessage(receiveMessageBlock)
    }

    private fun createWebSocketTask(url: String) = NSURLSession.sharedSession.webSocketTaskWithURL(NSURL(string = url))

    private fun prepareToReceiveIncomingMessage() {
        val receiveIncomingMessageCompletionHandler =
                { webSocketMessage: NSURLSessionWebSocketMessage?, error: NSError? ->
                    if (error != null) {
                        println("WebSocket couldn’t receive message because: $error")
                    } else {
                        produceWebSocketMessage(webSocketMessage?.string ?: "")
                        prepareToReceiveIncomingMessage()
                    }
                }.freeze()

        webSocketTask.receiveMessageWithCompletionHandler(receiveIncomingMessageCompletionHandler)
    }

    private fun produceWebSocketMessage(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            webSocketChannel.send(message)
        }
    }

    private fun consumeWebSocketMessage(block: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            for (message in webSocketChannel) {
                block(message)
            }
        }
    }

    fun sendMessage(message: String) {
        val sendCompletionHandler = { error: NSError? ->
            if (error != null) {
                println("WebSocket couldn’t send message because: $error")
            }
        }.freeze()

        webSocketTask.sendMessage(message.convertToWebSocketMessage(), sendCompletionHandler)
    }

}

private fun String.convertToWebSocketMessage() = NSURLSessionWebSocketMessage(this)
