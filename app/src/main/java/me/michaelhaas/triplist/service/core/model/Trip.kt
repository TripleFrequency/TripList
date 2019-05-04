package me.michaelhaas.triplist.service.core.model

import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver

data class Trip(
    val id: Int,
    val name: String,
    val thumbnail: ImageResolver,
    private val tripDetailsResolver: suspend () -> TripDetails?
) {
    private var _tripDetails: TripDetails? = null

    suspend fun getTripDetails() = _tripDetails ?: tripDetailsResolver()?.also { _tripDetails = it }
}