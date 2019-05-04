package me.michaelhaas.triplist.service.core

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.db.dao.PhotoDao
import me.michaelhaas.triplist.service.db.model.PhotoEntity
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao
) {

    suspend fun getResolvers(tripId: Int, urls: List<String>) =
        withContext(Dispatchers.IO) { urls.map { getResolver(tripId, it) } }

    suspend fun getResolvers(tripId: Int): List<ImageResolver> =
        withContext(Dispatchers.IO) { photoDao.getPhotos(tripId).map { getResolver(tripId, it.photoUrl) } }

    suspend fun getResolver(tripId: Int, url: String): ImageResolver {
        var existing: PhotoEntity? = null
        withContext(Dispatchers.IO) {
            existing = photoDao.getPhoto(tripId, url)
        }
        return existing?.encodedPhoto?.let {
            ImageResolver.CacheImageResolver(it)
        } ?: ImageResolver.WebImageResolver(Uri.parse(url)) {
            saveToDatabase(tripId, url, existing, it)
        }
    }

    private fun saveToDatabase(tripId: Int, url: String, existing: PhotoEntity?, it: Bitmap?) {
        val imageString = Base64.encodeToString(ByteArrayOutputStream().also { baos ->
            it?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        }.toByteArray(), Base64.DEFAULT)

        val update = existing?.copy(encodedPhoto = imageString)
        if (update != null) {
            photoDao.updatePhoto(update)
        } else {
            photoDao.insertPhoto(PhotoEntity(0, tripId, url, imageString))
        }
    }
}