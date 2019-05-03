package me.michaelhaas.triplist.service.core.model

import me.michaelhaas.triplist.service.http.model.TripActivity

sealed class TripDetails(
    val location: String,
    val description: String,
    val photos: List<Image>,
    val activities: List<TripActivity>
) {

}