package me.michaelhaas.triplist.service.core.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.db.ActivityRepository
import me.michaelhaas.triplist.service.core.PhotoRepository
import me.michaelhaas.triplist.service.db.model.TripDetailsEntity
import me.michaelhaas.triplist.service.http.model.TripActivity
import me.michaelhaas.triplist.service.http.model.TripDetailsContract
import me.michaelhaas.triplist.service.http.model.toTripActivity

data class TripDetails(
    val location: String,
    val description: String,
    val photos: List<ImageResolver>,
    val activities: List<TripActivity>
)

suspend fun TripDetailsEntity.toTripDetails(
    photoRepository: PhotoRepository,
    activityRepository: ActivityRepository,
    tripId: Int
) = withContext(Dispatchers.IO) {
    TripDetails(
        location,
        description,
        photoRepository.getResolvers(tripId),
        activityRepository.getAll(tripId).map { it.toTripActivity() })
}

suspend fun TripDetailsContract.toTripDetails(photoRepository: PhotoRepository) = withContext(Dispatchers.IO) {
    TripDetails(
        location,
        description,
        photoUrls.map { photoRepository.getResolver(id, it) },
        activities.map { it.toTripActivity() })
}