package com.example.careconnect.Database

import com.example.careconnect.Pages.Hidden.ChatMessageDto
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class UserRepository {
    // Configure Realm with the schema of our RealmUser model.
    private val config = RealmConfiguration.create(schema = setOf(RealmUser::class,Chats::class,ChatMessage::class))
    private val realm: Realm = Realm.open(config)



    // Assume your RealmUser has an id field of type ObjectId
    suspend fun setUser(user: UserData) {

        val u = RealmUser().apply {
            username = user.username ?: "was empty"
            email = user.email ?: "was empty"
            password = user.password ?: "was empty"
            token = user.token ?: "was empty"
        }
        realm.write {
            // Look for the existing user by their ObjectId
            val existingUser = query<RealmUser>("username == $0", user.username).first().find()
            if (existingUser != null) {
                // Update the fields.
                existingUser.username = user.username  ?: "was empty"
                existingUser.email = user.email ?: "was empty"
                existingUser.password = user.password ?: "was empty"
                existingUser.token = user.token  ?: "was empty"
            } else {
                println("Output : ${u.toUserdata()}")
                println("Transacted user : ${copyToRealm(u)}")
            }
        }
    }


    // Retrieves the single user.
    fun getUser(): RealmUser? {
        return realm.query<RealmUser>().first().find()
    }
    suspend fun deleteAllUsers() {
        realm.write {
            val allUsers = query<RealmUser>().find()
            delete(allUsers)
        }
    }

    fun getChats(chatid: String): List<ChatMessageDto> {
        val chat = realm.query<Chats>("chatid == $0", chatid)
            .first()
            .find()
        return chat?.messagelist?.toList()?.map { it.toChat() } ?: emptyList()
    }
    suspend fun updateChats(chatid: String, chats: List<ChatMessageDto>) {
        realm.write {
            // Query for the Chats object with the provided chatid
            val chat = query<Chats>("chatid == $0", chatid)
                .first()
                .find()

            // If the chat exists, update its message list
            chat?.messagelist?.apply {
                clear() // Clear existing messages
                addAll(chats.map { it.toRealmObject() })
            }
        }
    }
    fun getAllChats(): List<Chats> {
        return realm.query<Chats>().find().toList()
    }


}