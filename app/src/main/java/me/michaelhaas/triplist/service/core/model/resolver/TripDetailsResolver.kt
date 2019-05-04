package me.michaelhaas.triplist.service.core.model.resolver

import me.michaelhaas.triplist.service.core.model.TripDetails
import me.michaelhaas.triplist.service.core.model.toTripDetails
import me.michaelhaas.triplist.service.db.ActivityRepository
import me.michaelhaas.triplist.service.core.PhotoRepository
import me.michaelhaas.triplist.service.db.model.TripDetailsEntity
import me.michaelhaas.triplist.service.http.TripListApi
import java.io.IOException

sealed class TripDetailsResolver : BaseResolver<TripDetails>() {

    class WebTripDetailsResolver(
        private val photoRepository: PhotoRepository,
        private val api: TripListApi,
        val tripId: Int,
        private val callback: WebResolverCallback<TripDetails>? = null
    ) : TripDetailsResolver() {
        override suspend fun resolve(): TripDetails? =
            try {
                api.getTripDetails(tripId).execute().body()?.toTripDetails(photoRepository)
            } catch (e: IOException) {
                null
            }
    }

    class CacheTripDetailsResolver(
        private val photoRepository: PhotoRepository,
        private val activityRepository: ActivityRepository,
        private val tripId: Int,
        val tripDetails: TripDetailsEntity
    ) : TripDetailsResolver() {
        override suspend fun resolve(): TripDetails? =
            tripDetails.toTripDetails(photoRepository, activityRepository, tripId)
    }
}