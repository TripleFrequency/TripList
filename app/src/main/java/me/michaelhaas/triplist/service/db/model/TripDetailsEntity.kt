package me.michaelhaas.triplist.service.db.model

import androidx.room.ColumnInfo

data class TripDetailsEntity(
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "description") val description: String
)