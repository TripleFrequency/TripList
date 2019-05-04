package me.michaelhaas.triplist.service.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.michaelhaas.triplist.service.db.util.RoomDateConverter
import java.util.*

@Entity(tableName = "user_trips")
@TypeConverters(RoomDateConverter::class)
data class UserTripEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "endDate") val endDate: Date
)