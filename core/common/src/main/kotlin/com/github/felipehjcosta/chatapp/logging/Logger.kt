package com.github.felipehjcosta.chatapp.logging

object Logger {

    private var isLoggable: Boolean = false

    private var loggerAdapter: LoggerAdapter? = null

    fun info(message: String) {
        if (isLoggable) {
            loggerAdapter?.info(message)
        }
    }

    operator fun invoke(block: LoggerConfiguration.() -> Unit) {
        LoggerConfiguration().apply {
            block(this)
            this@Logger.isLoggable = this.isLoggable
            this@Logger.loggerAdapter = this.loggerAdapter
        }
    }

    class LoggerConfiguration {
        var isLoggable: Boolean = false
        var loggerAdapter: LoggerAdapter? = null
    }

}
