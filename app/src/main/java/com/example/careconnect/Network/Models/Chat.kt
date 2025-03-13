package com.example.careconnect.Network.Models

import com.example.careconnect.Database.ChatMessageDto
import kotlinx.serialization.Serializable


@Serializable
data class UplinkOne(
    val message : String,
)

@Serializable
data class Uplink(
    val message: String,
    val chatid: String? = null
)


@Serializable
data class Downlink(
    val message : String?,
    val chatid : String? = null
)

fun Downlink.toDto() = this.message?.let { this.chatid?.let { it1 -> DownlinkDto(it, it1) } }

data class DownlinkDto(
    val message: String,
    val chatid: String
)

fun DownlinkDto.toChatMessage() = ChatMessageDto(this.message,false)