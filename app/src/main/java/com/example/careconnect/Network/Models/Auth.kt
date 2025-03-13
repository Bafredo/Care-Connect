package com.example.careconnect.Network.Models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username : String? = null,
    val password : String? = null,
)

@Serializable
data class RegisterUser(
    val username: String,
    val email: String,
    val password: String
)

fun RegisterUser.toDto() = RegisterUserDto(this.username,this.email,this.password)
fun RegisterUserDto.toModel() = RegisterUser(this.username,this.email,this.password)

data class RegisterUserDto(
    val username: String,
    val email: String,
    val password: String
)
@Serializable
data class RequestNewToken(
    val email: String
)
@Serializable
data class ValidateOtp(
    val email: String,
    val code : String
)

@Serializable
data class RecievedUser(
    val message : String? = null,
    val token : String? = null,
    val user : SubUser? = SubUser(null,null,null,null)
)




@Serializable
data class SubUser(
    val id : String?,
    val email : String?,
    val username: String?,
    val role : String?
)


@Serializable
data class Doctor(
    val id : String,
    val phone : String? = null,
    val email : String,
    val full_name : String,
    val specialty : List<String>
)

fun Doctor.toDto() = DoctorDto(
    this.id,
    this.phone,
    this.email,
    this.full_name,
    this.specialty
)

data class DoctorDto(
    val id : String,
    val phone : String? = null,
    val email : String,
    val full_name : String,
    val specialty : List<String>
)
