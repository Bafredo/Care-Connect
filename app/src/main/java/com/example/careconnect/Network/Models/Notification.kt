package com.example.careconnect.Network.Models

import kotlinx.serialization.Serializable
@Serializable
data class NotificationBody(
    val title : String,
    val body : String
)
@Serializable
data class Token(
    val fcm : String
)