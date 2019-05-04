package me.michaelhaas.triplist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_details.*
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver

class TripDetailsActivity : AppCompatActivity() {

    private var tripId: Int = 0
    private var thumbnailUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        tripId = intent?.getIntExtra(EXTRA_TRIP_ID, 0) ?: 0
        thumbnailUrl = intent?.getStringExtra(EXTRA_TRIP_THUMBNAIL_USE_CACHED)

        if (tripId == 0) {
            throw IllegalStateException("No value for tripId provided, or invalid: $tripId")
        }

        if (thumbnailUrl != null) {
            val thumbnailResolver = ImageResolver(Uri.parse(thumbnailUrl))
            thumbnailResolver.resolveInto(trip_thumbnail)
        }
    }

    class Builder(
        var tripId: Int,
        var transitionThumbnailUrl: String? = null,
        var doImageTransition: Boolean = false,
        vararg val sharedComponents: androidx.core.util.Pair<View, String>
    ) {
        fun buildIntent(context: Context): Pair<Intent, Bundle?> {
            val intent = Intent(context, TripDetailsActivity::class.java).apply {
                putExtra(EXTRA_TRIP_ID, tripId)
                putExtra(EXTRA_TRIP_THUMBNAIL_USE_CACHED, transitionThumbnailUrl)
            }
            val options = if (doImageTransition && sharedComponents.isNotEmpty() && context is Activity) {
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context,
                    *sharedComponents
                ).toBundle()
            } else {
                null
            }
            return intent to options
        }
    }

    companion object {
        private const val EXTRA_TRIP_ID = "tripId"
        private const val EXTRA_TRIP_THUMBNAIL_USE_CACHED = "tripThumbnailUrl"
    }
}