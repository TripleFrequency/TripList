package me.michaelhaas.triplist.service.db.dao

import androidx.room.*
import me.michaelhaas.triplist.service.db.model.PhotoEntity

@Dao
abstract class PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhoto(photo: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM trip_photos WHERE trip_id = :tripId AND photo_url = :photoUrl LIMIT 1;")
    abstract fun getPhoto(tripId: Int, photoUrl: String): PhotoEntity?

    @Query("SELECT * FROM trip_photos WHERE trip_id = :tripId LIMIT 1;")
    abstract fun getPhotos(tripId: Int): List<PhotoEntity>

    @Update
    abstract fun updatePhoto(photo: PhotoEntity)

    @Query("DELETE FROM trip_photos WHERE trip_id = :tripId;")
    abstract fun deleteFor(tripId: Int)
}