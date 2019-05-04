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

class AllTripsViewModel @Inject constructor(
    tripRepo: TripRepository
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val mutableAllTripsLiveData = MutableLiveData<List<Trip>>()
    val allTripsLiveData: LiveData<List<Trip>> = mutableAllTripsLiveData

    init {
        scope.launch(Dispatchers.IO) {
            mutableAllTripsLiveData.postValue(tripRepo.getTrips())
        }
    }
}