package me.michaelhaas.triplist.ui.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.michaelhaas.triplist.service.core.UserTripRepository
import me.michaelhaas.triplist.service.db.model.UserTripEntity
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TripEditorViewModel @Inject constructor(
    private val userTripRepo: UserTripRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    suspend fun insertUserTrip(tripId: Int, startDate: Date, endDate: Date) =
        userTripRepo.createUserTripAsync(UserTripEntity(0, tripId, startDate, endDate)).await()

    suspend fun updateUserTrip(userTripId: Int, startDate: Date, endDate: Date) =
        userTripRepo.updateTrip(userTripId, startDate, endDate).join()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}