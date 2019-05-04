package me.michaelhaas.triplist.service.db.util

import androidx.room.TypeConverter
import java.util.*

class RoomDateConverter {
    @TypeConverter
    fun toTimestamp(date: Date) = date.time

    @TypeConverter
    fun fromTimestamp(time: Long) = Date(time)
}