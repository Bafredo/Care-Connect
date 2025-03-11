package com.example.careconnect.Database

import com.example.careconnect.Pages.Hidden.ChatMessageDto
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


open class RealmUser : RealmObject {
    @PrimaryKey var id : ObjectId = ObjectId()
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var token: String = ""
}

open class Chats : RealmObject {
    @PrimaryKey
    var chatid: String = ""
    var messagelist: RealmList<ChatMessage> = realmListOf()
}
open class ChatMessage : RealmObject{
    var text: String= ""
    var isSent: Boolean = true
}

data class ChatsDto(
    val chatid : String,
    val messageList: List<ChatMessageDto>
)

fun Chats.toDto() = ChatsDto(
    this.chatid,
    this.messagelist.toList().map { it.toChat() }
)

fun ChatMessage.toChat() = ChatMessageDto(
    this.text,
    this.isSent
)

fun ChatMessageDto.toRealmObject(): ChatMessage {
    return ChatMessage().apply {
        text = this@toRealmObject.text
        isSent = this@toRealmObject.isSent
    }
}