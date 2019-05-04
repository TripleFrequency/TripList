package me.michaelhaas.triplist.service.core.model

import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.http.model.TripActivity

data class TripDetails(
    val location: String,
    val description: String,
    val photos: List<ImageResolver>,
    val activities: List<TripActivity>
)