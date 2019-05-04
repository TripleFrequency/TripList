package me.michaelhaas.triplist.service.http.model

import me.michaelhaas.triplist.service.db.model.ActivityEntity

data class TripActivity(
    val name: String,
    val type: String,
    val description: String
)

fun ActivityEntity.toTripActivity() = TripActivity(name, type, description)

fun ActivityContract.toTripActivity() = TripActivity(name, type, description)