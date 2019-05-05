package me.michaelhaas.triplist.service.core

import android.net.Uri
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.michaelhaas.triplist.analytics.AnalyticsBuilder
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.db.dao.TripDao
import me.michaelhaas.triplist.service.db.model.TripEntity
import me.michaelhaas.triplist.service.http.TripListApi
import me.michaelhaas.triplist.service.http.model.TripContract
import java.io.IOException
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val tripDao: TripDao,
    private val detailsRepository: TripDetailsRepository,
    private val webApi: TripListApi,
    private val analytics: FirebaseAnalytics
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val tripMutex = Mutex()
    private var tripCache: List<Trip>? = null

    suspend fun resetMemoryCache() = tripMutex.withLock {
        tripCache = null
    }

    suspend fun getTrips() = tripMutex.withLock {
        return@withLock tripCache ?: withContext(Dispatchers.IO) {
            val dbTrips = tripDao.getTrips()
            return@withContext if (dbTrips.isEmpty()) {
                loadTripsFromWeb()
            } else {
                dbTrips.map {
                    Trip(it.id, it.name, getThumbnailResolver(it.thumbnailUrl)) {
                        detailsRepository.getDetails(it.id)
                    }
                }
            }.also {
                tripCache = it
            }
        }
    }

    suspend fun getTrip(tripId: Int) = (tripCache ?: getTrips()).find { it.id == tripId }
        ?: throw IllegalStateException("No trip found for id $tripId")

    private suspend fun loadTripsFromWeb(): List<Trip> =
        try {
            webApi.getTrips().execute().body()?.let { getTrip(it) }.also {
                AnalyticsBuilder.EndpointCalledEvent.AllTripsCalledEvent().log(analytics)
            }
        } catch (e: IOException) {
            null
        } ?: emptyList()

    private suspend fun getTrip(contracts: List<TripContract>): List<Trip> {
        scope.launch(Dispatchers.IO) {
            insertContracts(contracts)
        }
        return contracts.map {
            Trip(it.id, it.name, getThumbnailResolver(it.thumbnailUrl)) {
                detailsRepository.getDetails(it.id)
            }
        }
    }

    private fun insertContracts(contracts: List<TripContract>) {
        tripDao.insertTrips(contracts.map {
            TripEntity(it.id, it.name, it.thumbnailUrl, null)
        })
    }

    private fun getThumbnailResolver(url: String) = ImageResolver(Uri.parse(url))
}