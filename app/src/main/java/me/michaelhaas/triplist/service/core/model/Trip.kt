package me.michaelhaas.triplist.service.core.model

import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver

data class Trip(
    val id: Int,
    val name: String,
    val thumbnail: ImageResolver,
    val tripDetails: TripDetails
)