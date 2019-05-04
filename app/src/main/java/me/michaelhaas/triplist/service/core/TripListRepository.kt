package me.michaelhaas.triplist.service.core

import android.net.Uri
import kotlinx.coroutines.*
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.service.core.model.TripDetails
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
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

    suspend fun getTrips() = withContext(Dispatchers.IO) {
        var dbTrips = tripDao.getTrips()
        if (dbTrips.isEmpty()) {

        }
    }

    private suspend fun loadTripsFromWeb(): List<TripContract> =
        try {
            webApi.getTrips().execute().body()
        } catch (e: IOException) {
            null
        } ?: emptyList()

    private suspend fun getTrip(contracts: List<TripContract>): List<Trip> {
        scope.launch(Dispatchers.IO) {
            insertContracts(contracts)
        }
        return contracts.map {
            Trip(it.id, it.name, ImageResolver.WebImageResolver(Uri.parse(it.thumbnailUrl), null), null)
        }
    }

    private fun insertContracts(contracts: List<TripContract>) {
        tripDao.insertTrips(contracts.map {
            TripEntity(it.id, it.name, it.thumbnailUrl, null, null)
        })
    }
}