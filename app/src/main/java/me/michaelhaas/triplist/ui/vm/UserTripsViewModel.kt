package me.michaelhaas.triplist.ui.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.michaelhaas.triplist.service.core.UserTripRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserTripsViewModel @Inject constructor(
    private val userTripRepo: UserTripRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val userTripLiveData = userTripRepo.userTrips

    fun refresh() = userTripRepo.refreshLiveData()

    fun deleteTrip(userTripId: Int) = userTripRepo.deleteTrip(userTripId)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}