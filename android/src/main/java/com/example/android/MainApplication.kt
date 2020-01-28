package com.example.android

import android.app.Application
import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.logging.LoggerAdapter

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger {
            loggerAdapter = object : LoggerAdapter {
                override fun info(message: String) {
                    android.util.Log.i("ANDROID", message)
                }
            }
        }

        ChatInjector {
            baseUrl = "${BuildConfig.CHAT_URL_SERVER}/chat"
        }
    }
}
