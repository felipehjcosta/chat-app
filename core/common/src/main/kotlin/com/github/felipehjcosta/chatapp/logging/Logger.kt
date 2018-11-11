package com.github.felipehjcosta.chatapp.logging

object Logger {

    private var loggerAdapter: LoggerAdapter? = null

    fun info(message: String) {
        loggerAdapter?.info(message)
    }

    operator fun invoke(block: LoggerConfiguration.() -> Unit) {
        LoggerConfiguration().apply {
            block(this)
            this@Logger.loggerAdapter = this.loggerAdapter
        }
    }

    class LoggerConfiguration {
        var loggerAdapter: LoggerAdapter? = null
    }

}
