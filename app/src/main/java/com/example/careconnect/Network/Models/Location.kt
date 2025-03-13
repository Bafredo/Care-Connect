package com.example.careconnect.Network.Models

import kotlinx.serialization.Serializable


@Serializable
data class Location(
    val longitude : Double,
    val latitude : Double
)