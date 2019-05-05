package me.michaelhaas.triplist.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.michaelhaas.triplist.service.core.TripRepository
import me.michaelhaas.triplist.service.core.model.Trip
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TripDetailsViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val mutableTripLiveData = MutableLiveData<Trip>()
    val tripLiveData: LiveData<Trip> = mutableTripLiveData

    fun getTrip(tripId: Int) = tripLiveData.also {
        launch(Dispatchers.IO) {
            mutableTripLiveData.postValue(tripRepository.getTrip(tripId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}