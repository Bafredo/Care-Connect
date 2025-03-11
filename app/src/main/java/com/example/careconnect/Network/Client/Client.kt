package com.example.careconnect.Network.Client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object KtorClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
}