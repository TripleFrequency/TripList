package me.michaelhaas.triplist.service.http.model

data class TripContract(
    val id: Int,
    val name: String,
    val thumbnailUrl: String
)