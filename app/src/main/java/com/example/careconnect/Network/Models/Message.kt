package com.example.careconnect.Network.Models

import com.example.careconnect.Database.ChatMessageDto
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

@Serializable
data class FirestoreMessage(
    var participants : List<Participant> = emptyList(),
    var messages : List<MessageItem> = emptyList(),
    var createdAt : Long = 0L
)

fun MessageItem.toChatMessageDto(userid : String) = ChatMessageDto(
    text = this.message,
    isSent = this.senderid == userid
)
@Serializable
data class MessageItem(
    var message : String = "",
    var senderid : String = "",
    val timestamp : Long = 0L
)
@Serializable
data class Participant(
    var id : String = "",
    var name : String = ""
)