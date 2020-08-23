package com.github.felipehjcosta.chatapp.logging

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object Logger {

    private var loggerAdapter: LoggerAdapter? = createDefaultLoggerAdapter()

    fun info(message: String) {
        loggerAdapter?.info(message)
    }

    operator fun invoke(block: LoggerConfiguration.() -> Unit) {
        LoggerConfiguration().apply {
            block(this)
            loggerAdapter = this.loggerAdapter
        }
    }

    class LoggerConfiguration {
        var loggerAdapter: LoggerAdapter? = null
    }

    private fun createDefaultLoggerAdapter(): LoggerAdapter = object : LoggerAdapter {

        override fun info(message: String) {
            println(message)
        }
    }
}
