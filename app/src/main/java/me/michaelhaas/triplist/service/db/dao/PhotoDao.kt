package me.michaelhaas.triplist.service.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.michaelhaas.triplist.service.db.model.PhotoEntity

@Dao
abstract class PhotoDao {

    @Insert
    abstract fun insertPhoto(photo: PhotoEntity)

    @Query("SELECT * FROM trip_photos WHERE trip_id = :tripId AND photo_url = :photoUrl LIMIT 1;")
    abstract fun getPhoto(tripId: Int, photoUrl: String): PhotoEntity?

    @Query("SELECT * FROM trip_photos WHERE trip_id = :tripId LIMIT 1;")
    abstract fun getPhotos(tripId: Int): List<PhotoEntity>

    @Update
    abstract fun updatePhoto(photo: PhotoEntity)
}