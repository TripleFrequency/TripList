package me.michaelhaas.triplist.service.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import me.michaelhaas.triplist.service.core.model.UserTrip
import me.michaelhaas.triplist.service.db.dao.UserTripDao
import me.michaelhaas.triplist.service.db.model.UserTripEntity
import java.util.*
import javax.inject.Inject

class UserTripRepository @Inject constructor(
    private val userTripDao: UserTripDao,
    private val tripRepository: TripRepository
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
            }
        }

    fun updateTrip(userTripId: Int, startDate: Date, endDate: Date): Job =
        scope.launch {
            userTripDao.updateUserTrip(userTripId, startDate, endDate)
            refreshLiveData().join()
        }

    fun deleteTrip(userTripId: Int): Job = scope.launch { userTripDao.deleteUserTrip(userTripId) }

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