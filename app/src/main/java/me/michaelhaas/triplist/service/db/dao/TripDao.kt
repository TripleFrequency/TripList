package me.michaelhaas.triplist.service.db.dao

import androidx.room.*
import me.michaelhaas.triplist.service.db.model.TripEntity

@Dao
abstract class TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTrip(trip: TripEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTrips(trips: List<TripEntity>)

    @Query("SELECT * FROM trips;")
    abstract fun getTrips(): List<TripEntity>

    @Query("SELECT * FROM trips WHERE id = :tripId LIMIT 1;")
    abstract fun getTrip(tripId: Int): TripEntity?

    @Update
    abstract fun updateTrip(trip: TripEntity)
}