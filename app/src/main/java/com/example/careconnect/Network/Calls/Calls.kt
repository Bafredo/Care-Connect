package com.example.careconnect.Network.Calls

import com.example.careconnect.Network.Client.KtorClient
import com.example.careconnect.Network.Models.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

object base {
    const val Url = "https://care-connect-backend-one.vercel.app/api/v1"
}

@Serializable
data class ResponseBody(
    val message: String
)


suspend fun register(user: RegisterUserDto): Int {
    return try {
        val response = KtorClient.client.post("${base.Url}/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(user.toModel())
        }

        val body: ResponseBody = response.body() // Ensure full response is received
        println("Response: $body")
        response.status.value
    } catch (e: Exception) {
        e.printStackTrace()
        0 // Return a custom error code if the request fails
    }
}


suspend fun login(user: User): RecievedUser {
    return try {
        val response: HttpResponse = KtorClient.client.post("${base.Url}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        val responseBody = response.body<RecievedUser>()
        println("Login Successful: $responseBody")
        responseBody
    } catch (e: Exception) {
        println("Login failed : ${e.localizedMessage}")
       RecievedUser()

    }
}


suspend fun requestNewToken(mail: String) {
    try {
        KtorClient.client.post("${base.Url}/auth/req-otp") {
            contentType(ContentType.Application.Json)
            setBody(RequestNewToken(mail))
        }
        println("OTP Request Sent Successfully")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


suspend fun validateToken(mail: String, otp: String, success: () -> Unit): Int {
    return try {
        val response = KtorClient.client.post("${base.Url}/auth/validate-code") {
            contentType(ContentType.Application.Json)
            setBody(ValidateOtp(mail, otp))
        }

        val body: ResponseBody = response.body() // Ensure response is read
        println("Validation Response: $body")

        if (response.status.value == 200) {
            success()
        }
        response.status.value
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}
suspend fun updateRemote(token: String,autht : String): Int? {
    return try {
        val response = KtorClient.client.post("${base.Url}/user/fcm") {
            header("Authorization", "Bearer $autht")
            contentType(ContentType.Application.Json)
            setBody(Token(token))
        }
        val body: ResponseBody = response.body() // Ensure response is read
        println("Validation Response: $body")
        response.status.value
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun chatBot(u : String, chatid : String? = null,autht: String ) : Downlink {
    return try {
        println("sending message :$u")
        val response = KtorClient.client.post("${base.Url}/chat/ai") {
            header("Authorization", "Bearer $autht")
            contentType(ContentType.Application.Json)
            setBody( if(chatid.isNullOrEmpty()) UplinkOne(u) else Uplink(u,chatid))
        }
        println("Got response ${response.body<Downlink>()}")
        val body: Downlink = response.body()
        body
    } catch (e: Exception) {
        e.printStackTrace()
        Downlink(null ,null)
    }
}

