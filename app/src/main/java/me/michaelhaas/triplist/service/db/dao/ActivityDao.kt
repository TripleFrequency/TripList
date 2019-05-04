package me.michaelhaas.triplist.service.db.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.michaelhaas.triplist.service.db.model.ActivityEntity

abstract class ActivityDao {

    @Insert
    abstract fun insertActivity(activity: ActivityEntity)

    @Insert
    abstract fun insertActivities(activities: List<ActivityEntity>)

    @Query("SELECT* FROM trip_activities WHERE trip_id = :tripId;")
    abstract fun getActivities(tripId: Int): List<ActivityEntity>

    @Update
    abstract fun updateActivity(activity: ActivityEntity)
}