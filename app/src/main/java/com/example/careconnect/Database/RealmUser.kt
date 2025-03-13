package com.example.careconnect.Database

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
    var name : String  = ""
    var messagelist: RealmList<ChatMessage> = realmListOf()
    var recieverid : String = ""
}
open class ChatMessage : RealmObject{
    var text: String= ""
    var isSent: Boolean = true
}

data class ChatsDto(
    val chatid : String,
    val name : String?  = null,
    val messageList: List<ChatMessageDto>,
    var recieverid : String? = null
)

fun Chats.toDto() = ChatsDto(
    chatid = this.chatid,
    messageList = this.messagelist.toList().map { it.toChat() },
    recieverid = this.recieverid
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