package me.michaelhaas.triplist.ui.util

import android.content.Context
import android.content.res.Configuration
import javax.inject.Inject

class NightModeUtil @Inject constructor(
    private val context: Context
) {
    val isNightEnabled =
        (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}