package me.michaelhaas.triplist.service.http.di

import dagger.Module
import dagger.Provides
import me.michaelhaas.triplist.service.http.TripListApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class HttpModule {
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(TRIP_LIST_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    fun provideTripListApi(retrofit: Retrofit): TripListApi = retrofit.create(TripListApi::class.java)

    companion object {
        const val TRIP_LIST_BASE_URL = "https://my-json-server.typicode.com/TripleFrequency/trip-list/"
    }
}