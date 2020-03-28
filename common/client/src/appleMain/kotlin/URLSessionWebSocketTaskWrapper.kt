package com.github.felipehjcosta.chatapp.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

    fun start(url: String, receiveMessageBlock: (String) -> Unit) {
        webSocketTask = createWebSocketTask(url)
        prepareToReceiveIncomingMessage(receiveMessageBlock)
        webSocketTask.resume()
    }

    private fun createWebSocketTask(url: String) = NSURLSession.sharedSession.webSocketTaskWithURL(NSURL(string = url))

    private fun prepareToReceiveIncomingMessage(receiveMessageBlock: (String) -> Unit) {
        val receiveIncomingMessageCompletionHandler =
                { webSocketMessage: NSURLSessionWebSocketMessage?, error: NSError? ->
                    if (error != null) {
                        println("WebSocket couldn’t receive message because: $error")
                    }

                    val message = webSocketMessage?.string ?: ""
                    GlobalScope.launch(Dispatchers.Main) {
                        receiveMessageBlock(message)
                    }


                    prepareToReceiveIncomingMessage(receiveMessageBlock)
                }.freeze()

        webSocketTask.receiveMessageWithCompletionHandler(receiveIncomingMessageCompletionHandler)
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
