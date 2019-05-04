package me.michaelhaas.triplist.service.core

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.db.dao.PhotoDao
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao
) {

    suspend fun getResolvers(urls: List<String>) =
        withContext(Dispatchers.IO) { urls.map { getResolver(it) } }

    suspend fun getResolvers(tripId: Int): List<ImageResolver> =
        withContext(Dispatchers.IO) { photoDao.getPhotos(tripId).map { getResolver(it.photoUrl) } }

    private fun getResolver(url: String): ImageResolver = ImageResolver(Uri.parse(url))
}