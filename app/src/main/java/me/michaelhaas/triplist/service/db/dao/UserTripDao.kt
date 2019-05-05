package me.michaelhaas.triplist.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import me.michaelhaas.triplist.service.db.model.UserTripEntity

@Dao
abstract class UserTripDao {

    @Update
    abstract fun insertTrip(userTripEntity: UserTripEntity)

    @Query("SELECT * FROM user_trips WHERE id = :userTripId")
    abstract fun getUserTrip(userTripId: Int): UserTripEntity

    @Query("SELECT * FROM user_trips;")
    abstract fun getUserTrips(): LiveData<List<UserTripEntity>>

    @Query("SELECT * FROM user_trips WHERE trip_id = :tripId")
    abstract fun getUserTripsFor(tripId: Int): List<UserTripEntity>

    @Update
    abstract fun updateUserTrip(userTripEntity: UserTripEntity)

    @Delete
    abstract fun deleteUserTrip(userTripEntity: UserTripEntity)
}