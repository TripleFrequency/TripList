package me.michaelhaas.triplist.service.db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.michaelhaas.triplist.service.db.TripListDatabase

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Context) = TripListDatabase(context)
}