package me.michaelhaas.triplist.service.db.dao

import androidx.room.*
import me.michaelhaas.triplist.service.db.model.ActivityEntity

@Dao
abstract class ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertActivity(activity: ActivityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertActivities(activities: List<ActivityEntity>)

    @Query("SELECT* FROM trip_activities WHERE trip_id = :tripId;")
    abstract fun getActivities(tripId: Int): List<ActivityEntity>

    @Update
    abstract fun updateActivity(activity: ActivityEntity)

    @Query("DELETE FROM trip_activities WHERE trip_id = :tripId;")
    abstract fun deleteFor(tripId: Int)
}