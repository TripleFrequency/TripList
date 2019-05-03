package me.michaelhaas.triplist.service

import me.michaelhaas.triplist.service.db.TripListDatabaseAdapter
import me.michaelhaas.triplist.service.http.TripListWebServiceAdapter
import javax.inject.Inject

class TripListRepository @Inject constructor(
    private val webAdapter: TripListWebServiceAdapter,
    private val databaseAdapter: TripListDatabaseAdapter
) {

}