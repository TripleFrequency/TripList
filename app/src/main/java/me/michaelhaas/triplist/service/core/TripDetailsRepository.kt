package me.michaelhaas.triplist.service.core

import kotlinx.coroutines.*
import me.michaelhaas.triplist.service.core.model.TripDetails
import me.michaelhaas.triplist.service.db.ActivityRepository
import me.michaelhaas.triplist.service.db.TripListDatabase
import me.michaelhaas.triplist.service.db.dao.ActivityDao
import me.michaelhaas.triplist.service.db.dao.PhotoDao
import me.michaelhaas.triplist.service.db.dao.TripDao
import me.michaelhaas.triplist.service.db.model.ActivityEntity
import me.michaelhaas.triplist.service.db.model.PhotoEntity
import me.michaelhaas.triplist.service.db.model.TripDetailsEntity
import me.michaelhaas.triplist.service.db.model.TripEntity
import me.michaelhaas.triplist.service.http.TripListApi
import me.michaelhaas.triplist.service.http.model.TripDetailsContract
import me.michaelhaas.triplist.service.http.model.toTripActivity
import java.io.IOException
import javax.inject.Inject

class TripDetailsRepository @Inject constructor(
    private val webApi: TripListApi,
    private val tripDb: TripListDatabase,
    private val tripDao: TripDao,
    private val photoDao: PhotoDao,
    private val activityDao: ActivityDao,
    private val photoRepository: PhotoRepository,
    private val activityRepository: ActivityRepository
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    suspend fun getDetails(tripId: Int) = withContext(Dispatchers.IO) {
        val existing = tripDao.getTrip(tripId)
        return@withContext existing?.let { getDetails(it) }
    }

    private suspend fun getDetails(entity: TripEntity): TripDetails? = entity.details?.let {
        TripDetails(
            it.location,
            it.description,
            photoRepository.getResolvers(entity.id),
            activityRepository.getAll(entity.id).map { activityEntity -> activityEntity.toTripActivity() })
    } ?: loadDetailsFromWeb(entity.id)

    private suspend fun loadDetailsFromWeb(tripId: Int): TripDetails? =
        try {
            webApi.getTripDetails(tripId).execute().body()?.let { getDetails(it) }
        } catch (e: IOException) {
            null
        }

    private suspend fun getDetails(contract: TripDetailsContract): TripDetails {
        scope.launch(Dispatchers.IO) {
            insertContract(contract)
        }
        return TripDetails(
            contract.location,
            contract.description,
            photoRepository.getResolvers(contract.id, contract.photoUrls),
            contract.activities.map { it.toTripActivity() }
        )
    }

    private fun insertContract(contract: TripDetailsContract) = tripDb.runInTransaction {
        val detailEntity = TripDetailsEntity(contract.location, contract.description)
        val existingEntity = tripDao.getTrip(contract.id)

        if (existingEntity != null) {
            tripDao.updateTrip(existingEntity.copy(details = detailEntity))

            photoDao.deleteFor(contract.id)
            photoDao.insertPhotos(contract.photoUrls.map { PhotoEntity(0, contract.id, it, null) })

            activityDao.deleteFor(contract.id)
            activityDao.insertActivities(contract.activities.map {
                ActivityEntity(
                    0,
                    contract.id,
                    it.name,
                    it.type,
                    it.description
                )
            })
        } else {
            throw IllegalStateException("Attempting to insert trip details for non-existent trip")
        }
    }
}