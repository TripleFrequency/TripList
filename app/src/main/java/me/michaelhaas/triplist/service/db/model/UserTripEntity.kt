package me.michaelhaas.triplist.service.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user_trips")
data class UserTripEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @ColumnInfo(name = "startDate") val startDate: LocalDate,
    @ColumnInfo(name = "endDate") val endDate: LocalDate
)