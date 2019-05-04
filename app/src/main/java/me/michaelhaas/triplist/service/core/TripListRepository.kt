package me.michaelhaas.triplist.service.core

import android.net.Uri
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.core.util.toBase64
import me.michaelhaas.triplist.service.db.dao.TripDao
import me.michaelhaas.triplist.service.db.model.TripEntity
import me.michaelhaas.triplist.service.http.TripListApi
import me.michaelhaas.triplist.service.http.model.TripContract
import java.io.IOException
import javax.inject.Inject

class TripListRepository @Inject constructor(
    private val tripDao: TripDao,
    private val detailsRepository: TripDetailsRepository,
    private val webApi: TripListApi
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val tripMutex = Mutex()
    private var tripCache: List<Trip>? = null

    suspend fun getTrips() = tripMutex.withLock {
        return@withLock tripCache ?: withContext(Dispatchers.IO) {
            val dbTrips = tripDao.getTrips()
            return@withContext if (dbTrips.isEmpty()) {
                loadTripsFromWeb()
            } else {
                dbTrips.map {
                    Trip(it.id, it.name, getThumbnailResolver(it.id, it.thumbnailUrl, it.encodedThumbnail)) {
                        detailsRepository.getDetails(it.id)
                    }
                }
            }.also {
                tripCache = it
            }
        }
    }

    private suspend fun loadTripsFromWeb(): List<Trip> =
        try {
            webApi.getTrips().execute().body()?.let { getTrip(it) }
        } catch (e: IOException) {
            null
        } ?: emptyList()

    private suspend fun getTrip(contracts: List<TripContract>): List<Trip> {
        scope.launch(Dispatchers.IO) {
            insertContracts(contracts)
        }
        return contracts.map {
            Trip(it.id, it.name, getThumbnailResolver(it.id, it.thumbnailUrl)) {
                detailsRepository.getDetails(it.id)
            }
        }
    }

    private fun insertContracts(contracts: List<TripContract>) {
        tripDao.insertTrips(contracts.map {
            TripEntity(it.id, it.name, it.thumbnailUrl, null, null)
        })
    }

    private fun getThumbnailResolver(tripId: Int, url: String, encoded: String? = null) =
        if (encoded == null) {
            ImageResolver.WebImageResolver(Uri.parse(url)) {
                it?.let { bmp ->
                    tripDao.updateTripThumbnail(tripId, bmp.toBase64())
                }
            }
        } else {
            ImageResolver.CacheImageResolver(encoded)
        }
}