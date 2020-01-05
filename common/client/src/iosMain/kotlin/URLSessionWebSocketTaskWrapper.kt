package com.github.felipehjcosta.chatapp.client

import platform.Foundation.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
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
        val receiveIncomingMessageCompletionHandler = { webSocketMessage: NSURLSessionWebSocketMessage?, error: NSError? ->
            if (error != null) {
                println("WebSocket couldn’t receive message because: $error")
            }

            val dispatchAsyncOnMainThread: platform.darwin.dispatch_block_t = {
                receiveMessageBlock(webSocketMessage?.string ?: "")
            }.freeze()

            dispatch_async(dispatch_get_main_queue(), dispatchAsyncOnMainThread)
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
