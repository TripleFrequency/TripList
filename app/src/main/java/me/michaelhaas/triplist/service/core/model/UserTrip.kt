package me.michaelhaas.triplist.service.core.model

import java.util.*

data class UserTrip(
    val id: Int = 0,
    val trip: Trip,
    val startDate: Date,
    val endDate: Date
)