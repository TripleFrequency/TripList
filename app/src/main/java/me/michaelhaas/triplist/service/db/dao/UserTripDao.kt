package me.michaelhaas.triplist.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import me.michaelhaas.triplist.service.db.model.UserTripEntity
import me.michaelhaas.triplist.service.db.util.RoomDateConverter
import java.util.*

@Dao
abstract class UserTripDao {

    @Insert
    abstract fun insertTrip(userTripEntity: UserTripEntity): Long

    @Query("SELECT * FROM user_trips WHERE id = :userTripId")
    abstract fun getUserTrip(userTripId: Int): LiveData<UserTripEntity>

    @Query("SELECT * FROM user_trips;")
    abstract fun getUserTrips(): LiveData<List<UserTripEntity>>

    @Query("SELECT * FROM user_trips;")
    abstract fun getUserTripsBlocking(): List<UserTripEntity>

    @Query("SELECT * FROM user_trips WHERE trip_id = :tripId")
    abstract fun getUserTripsFor(tripId: Int): List<UserTripEntity>

    @Update
    abstract fun updateUserTrip(userTripEntity: UserTripEntity)

    @Query("UPDATE user_trips SET startDate = :startDate, endDate = :endDate WHERE id = :userTripId;")
    @TypeConverters(RoomDateConverter::class)
    abstract fun updateUserTrip(userTripId: Int, startDate: Date, endDate: Date)

    @Query("DELETE FROM user_trips WHERE id = :userTripId")
    abstract fun deleteUserTrip(userTripId: Int)
}