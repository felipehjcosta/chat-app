package com.github.felipehjcosta.chatapp

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Message(val author: String, val message: String)

fun Message.stringify(): String = Json.encodeToString(this)

fun String.toMessage(): Message = Json.decodeFromString(this)
