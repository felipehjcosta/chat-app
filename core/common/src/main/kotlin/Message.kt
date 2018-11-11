package com.github.felipehjcosta.chatapp

import kotlinx.serialization.Serializable

@Serializable
data class Message(val author: String, val message: String)