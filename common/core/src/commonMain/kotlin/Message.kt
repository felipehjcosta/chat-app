package com.github.felipehjcosta.chatapp

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Serializable
data class Message(val author: String, val message: String)

@UseExperimental(ImplicitReflectionSerializer::class)
fun Message.stringify(): String = Json.stringify(Message::class.serializer(), this)

@UseExperimental(ImplicitReflectionSerializer::class)
fun String.toMessage(): Message = Json.parse(Message::class.serializer(), this)