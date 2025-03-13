package com.example.careconnect.Network.Models

import kotlinx.serialization.Serializable


@Serializable
data class SendMessage(
    val message : String,
    val recieverid : String
)


fun SendMessageDto.toModel() = SendMessage(
    this.message,
    this.recieverid
)

data class SendMessageDto(
    val message : String,
    val recieverid : String
)


data class ChatId(
    val chatid: String,
    val recieverId : String
)

@Serializable
data class ChatResponse(
    val chatid: String,
    val message: String
)

fun ChatResponse.toDto()  = ChatResponseDto(
    this.chatid,
    this.message
)


data class ChatResponseDto(
    val chatid: String,
    val message: String
)