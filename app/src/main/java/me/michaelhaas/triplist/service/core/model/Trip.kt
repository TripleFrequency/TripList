package me.michaelhaas.triplist.service.core.model

data class Trip(
    val id: Int,
    val name: String,
    val thumbnail: Image,
    val tripDetails: TripDetails
)