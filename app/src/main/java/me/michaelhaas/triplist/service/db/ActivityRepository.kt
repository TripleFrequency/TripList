package me.michaelhaas.triplist.service.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.michaelhaas.triplist.service.db.dao.ActivityDao
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao
) {

    suspend fun getAll(tripId: Int) = withContext(Dispatchers.IO) { activityDao.getActivities(tripId) }
}