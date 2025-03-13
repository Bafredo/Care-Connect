package com.example.careconnect.Database



// Plain Kotlin data class.
data class UserData(
    val id: String? = "",
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val token: String? = null
)


// Convert RealmUser to UserData.
fun RealmUser.toUserdata() =
     UserData(
        id = this.id.toString(),
        username = this.username,
        email = this.email,
        password = this.password,
        token = this.token
    )

data class ChatMessageDto(val text: String, val isSent: Boolean)
