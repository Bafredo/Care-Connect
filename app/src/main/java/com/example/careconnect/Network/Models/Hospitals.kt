package com.example.careconnect.Network.Models

import kotlinx.serialization.Serializable


@Serializable
data class Hospitals(
    val hospitals : List<Hospital>
)
data class HospitalsDto(
     val hospitals: List<HospitalDto>
)

fun Hospitals.toDto() = HospitalsDto(
    this.hospitals.map { it.toDto() }
)

@Serializable
data class Hospital(
    val id : String,
    val name : String,
    val level : Int,
    val distance: Double
)

fun Hospital.toDto() = HospitalDto(
    this.id,
    this.name,
    this.level,
    this.distance
)
data class HospitalDto(
    val id : String,
    val name : String,
    val level : Int,
    val distance : Double
)