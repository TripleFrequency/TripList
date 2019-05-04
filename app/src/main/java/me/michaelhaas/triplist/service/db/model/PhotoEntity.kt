package me.michaelhaas.triplist.service.db.model

import androidx.room.*

@Entity(
    tableName = "trip_photos",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "trip_id", index = true) val tripId: Int,
    @ColumnInfo(name = "photo_url") val photoUrl: String,
    @ColumnInfo(name = "photo_encoded") val encodedPhoto: String?
)