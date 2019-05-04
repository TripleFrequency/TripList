package me.michaelhaas.triplist.service.http

import me.michaelhaas.triplist.service.http.model.TripContract
import me.michaelhaas.triplist.service.http.model.TripDetailsContract
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TripListApi {
    @GET("trips")
    fun getTrips(): Call<List<TripContract>>

    @GET("details/{tripId}")
    fun getTripDetails(@Path("tripId") tripId: Int): Call<TripDetailsContract>
}