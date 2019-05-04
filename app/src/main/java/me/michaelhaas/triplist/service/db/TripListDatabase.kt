package me.michaelhaas.triplist.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.michaelhaas.triplist.service.db.dao.ActivityDao
import me.michaelhaas.triplist.service.db.dao.PhotoDao
import me.michaelhaas.triplist.service.db.dao.TripDao
import me.michaelhaas.triplist.service.db.dao.UserTripDao
import me.michaelhaas.triplist.service.db.model.*

@Database(
    entities = [
        ActivityEntity::class,
        PhotoEntity::class,
        TripEntity::class,
        UserTripEntity::class
    ],
    version = 2019050401
)
abstract class TripListDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun activityDao(): ActivityDao
    abstract fun tripDao(): TripDao
    abstract fun userTripDao(): UserTripDao

    companion object {
        private const val TRIP_DATABASE_NAME = "trip-database"

        operator fun invoke(context: Context) =
            Room.databaseBuilder(
                context,
                TripListDatabase::class.java,
                TRIP_DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
    }
}