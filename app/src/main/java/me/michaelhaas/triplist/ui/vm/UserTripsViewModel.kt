package me.michaelhaas.triplist.ui.vm

import androidx.lifecycle.ViewModel
import me.michaelhaas.triplist.service.core.UserTripRepository
import javax.inject.Inject

class UserTripsViewModel @Inject constructor(
    userTripRepo: UserTripRepository
) : ViewModel() {

    val userTripLiveData = userTripRepo.userTrips
}