package me.michaelhaas.triplist.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.michaelhaas.triplist.service.db.dao.PhotoDao
import me.michaelhaas.triplist.service.db.model.*

@Database(
    entities = [
        ActivityEntity::class,
        PhotoEntity::class,
        TripDetailsEntity::class,
        TripEntity::class,
        UserTripEntity::class
    ],
    version = 2019050301
)
abstract class TripListDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        private const val TRIP_DATABASE_NAME = "trip-database"

        operator fun invoke(context: Context) =
            Room.databaseBuilder(context, TripListDatabase::class.java, TRIP_DATABASE_NAME)
    }
}