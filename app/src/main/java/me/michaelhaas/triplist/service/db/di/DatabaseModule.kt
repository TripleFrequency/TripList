package me.michaelhaas.triplist.service.db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.michaelhaas.triplist.service.db.TripListDatabase

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Context) = TripListDatabase(context)

    @Provides
    fun providePhotoDao(database: TripListDatabase) = database.photoDao()

    @Provides
    fun provideActivityDao(database: TripListDatabase) = database.activityDao()

    @Provides
    fun provideTripDao(database: TripListDatabase) = database.tripDao()

    @Provides
    fun provideUserTripDao(database: TripListDatabase) = database.userTripDao()
}