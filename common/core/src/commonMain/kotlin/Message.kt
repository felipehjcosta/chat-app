package com.github.felipehjcosta.chatapp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
data class Message(val author: String, val message: String)

fun Message.stringify(): String = Json(JsonConfiguration.Stable).stringify(Message.serializer(), this)

fun String.toMessage(): Message = Json(JsonConfiguration.Stable).parse(Message.serializer(), this)
