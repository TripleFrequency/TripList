package me.michaelhaas.triplist.service.http

import javax.inject.Inject

class TripListWebServiceAdapter @Inject constructor(
    private val tripListApi: TripListApi
)