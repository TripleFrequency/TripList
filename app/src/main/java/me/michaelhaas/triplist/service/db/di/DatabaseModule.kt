package me.michaelhaas.triplist.service.db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.michaelhaas.triplist.service.db.TripListDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = TripListDatabase(context)

    @Provides
    @Singleton
    fun providePhotoDao(database: TripListDatabase) = database.photoDao()

    @Provides
    @Singleton
    fun provideActivityDao(database: TripListDatabase) = database.activityDao()

    @Provides
    @Singleton
    fun provideTripDao(database: TripListDatabase) = database.tripDao()

    @Provides
    @Singleton
    fun provideUserTripDao(database: TripListDatabase) = database.userTripDao()
}