package me.michaelhaas.triplist.service.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*
import me.michaelhaas.triplist.analytics.AnalyticsBuilder
import me.michaelhaas.triplist.service.core.model.UserTrip
import me.michaelhaas.triplist.service.db.dao.UserTripDao
import me.michaelhaas.triplist.service.db.model.UserTripEntity
import java.util.*
import javax.inject.Inject

class UserTripRepository @Inject constructor(
    private val userTripDao: UserTripDao,
    private val tripRepository: TripRepository,
    private val analytics: FirebaseAnalytics
) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val mutableUserTrips = MutableLiveData<List<UserTrip>>()
    val userTrips: LiveData<List<UserTrip>> = mutableUserTrips

    init {
        userTripDao.getUserTrips().observeForever {
            mapTrips(it)
        }
    }

    fun refreshLiveData() = scope.launch {
        mapTrips(userTripDao.getUserTripsBlocking())
    }

    fun getUserTrip(userTripId: Int) = userTripDao.getUserTrip(userTripId)

    fun getUserTripsOfIdAsync(tripId: Int) = scope.async { userTripDao.getUserTripsFor(tripId) }

    fun createUserTripAsync(trip: UserTripEntity) =
        scope.async {
            userTripDao.insertTrip(trip).also {
                refreshLiveData().join()
                AnalyticsBuilder.UserTripEvent.UserTripCreatedEvent(trip.tripId).log(analytics)
            }
        }

    fun updateTrip(userTripId: Int, tripId: Int, startDate: Date, endDate: Date): Job =
        scope.launch {
            userTripDao.updateUserTrip(userTripId, startDate, endDate)
            refreshLiveData().join()
            AnalyticsBuilder.UserTripEvent.UserTripUpdatedEvent(tripId).log(analytics)
        }

    fun deleteTrip(userTripId: Int, tripId: Int): Job = scope.launch {
        userTripDao.deleteUserTrip(userTripId)
        AnalyticsBuilder.UserTripEvent.UserTripDeletedEvent(tripId).log(analytics)
    }

    private fun mapTrips(it: List<UserTripEntity>) {
        scope.launch {
            mutableUserTrips.postValue(it.map { userTripEntity ->
                UserTrip(
                    userTripEntity.id,
                    tripRepository.getTrip(userTripEntity.tripId),
                    userTripEntity.startDate,
                    userTripEntity.endDate
                )
            })
        }
    }
}