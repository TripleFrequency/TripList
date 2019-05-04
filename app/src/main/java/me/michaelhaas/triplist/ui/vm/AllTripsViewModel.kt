package me.michaelhaas.triplist.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import me.michaelhaas.triplist.service.core.TripRepository
import me.michaelhaas.triplist.service.core.model.Trip
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AllTripsViewModel @Inject constructor(
    private val tripRepo: TripRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val mutableAllTripsLiveData = MutableLiveData<List<Trip>>()
    val allTripsLiveData: LiveData<List<Trip>> = mutableAllTripsLiveData

    init {
        launch(Dispatchers.IO) {
            mutableAllTripsLiveData.postValue(tripRepo.getTrips())
        }
    }

    suspend fun refreshTrips() {
        tripRepo.resetMemoryCache()
        withContext(Dispatchers.IO) {
            mutableAllTripsLiveData.postValue(tripRepo.getTrips())
        }
    }
}