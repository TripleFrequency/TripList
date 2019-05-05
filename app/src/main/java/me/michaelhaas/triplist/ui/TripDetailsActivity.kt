package me.michaelhaas.triplist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_details.*
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver
import me.michaelhaas.triplist.service.db.model.UserTripEntity
import me.michaelhaas.triplist.ui.util.NightModeUtil
import me.michaelhaas.triplist.ui.vm.TripDetailsViewModel
import javax.inject.Inject

class TripDetailsActivity : AppCompatActivity() {

    private var tripId: Int = 0
    private var userTripId: Int? = null
    private var thumbnailUrl: String? = null

    private var editorFragment: TripEditorFragment? = null

    @Inject
    lateinit var nightModeUtil: NightModeUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val tripDetailsViewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        )[TripDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar?.hide()

        tripId = intent?.getIntExtra(EXTRA_TRIP_ID, 0) ?: 0
        userTripId = intent?.getIntExtra(EXTRA_USER_TRIP_ID, 0)?.let {
            if (it == 0) {
                null
            } else {
                it
            }
        }
        thumbnailUrl = intent?.getStringExtra(EXTRA_TRIP_THUMBNAIL_USE_CACHED)

        if (tripId == 0) {
            throw IllegalStateException("No value for tripId provided, or invalid: $tripId")
        }

        tripDetailsViewModel.getTrip(tripId).observe(this, Observer {
            trip_title?.text = it.name
        })

        userTripId?.let { userTripId ->
            tripDetailsViewModel.getUserTrip(userTripId).observe(this, Observer { userTripEntity ->
                updateWithTrip(userTripEntity)
            })
        } ?: updateWithTrip()

        if (thumbnailUrl != null) {
            val thumbnailResolver = ImageResolver(Uri.parse(thumbnailUrl))
            thumbnailResolver.resolveInto(trip_thumbnail)
        }

        status_gradient?.setBackgroundResource(R.drawable.trip_title_gradient)

        window?.apply {
            if (nightModeUtil.isNightEnabled) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onBackPressed() {
        editorFragment?.let {
            editorFragment = null
            onEditorClosed()
        }
        super.onBackPressed()
    }

    fun onEditorClosed(newId: Int? = null) {
        if (newId != null) {
            trip_fab?.setImageResource(R.drawable.ic_edit_black_24dp)
        }
        trip_fab?.show()
    }

    private fun updateWithTrip(userTripEntity: UserTripEntity? = null) {
        if (userTripEntity == null) {
            trip_fab?.setImageResource(R.drawable.ic_add_black_24dp)
        } else {
            trip_fab?.setImageResource(R.drawable.ic_edit_black_24dp)
        }

        trip_fab?.setOnClickListener {
            val fragment = TripEditorFragment.Builder(
                userTripEntity?.id,
                userTripEntity?.startDate,
                userTripEntity?.endDate
            ) {
                trip_fab?.show()
                if (userTripEntity == null) {
                    trip_fab?.setImageResource(R.drawable.ic_edit_black_24dp)
                }
            }.build()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.trip_editor_container, fragment)
                .addToBackStack(TripEditorFragment.BACK_NAVIGATION_TAG)
                .commit()
            editorFragment = fragment

            trip_fab?.hide()
        }
    }

    class Builder(
        var tripId: Int,
        var userTripId: Int? = null,
        var transitionThumbnailUrl: String? = null,
        var doImageTransition: Boolean = false,
        vararg val sharedComponents: androidx.core.util.Pair<View, String>
    ) {
        fun buildIntent(context: Context): Pair<Intent, Bundle?> {
            val intent = Intent(context, TripDetailsActivity::class.java).apply {
                putExtra(EXTRA_TRIP_ID, tripId)
                userTripId?.let {
                    putExtra(EXTRA_USER_TRIP_ID, it)
                }
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
        private const val EXTRA_USER_TRIP_ID = "userTripId"
        private const val EXTRA_TRIP_THUMBNAIL_USE_CACHED = "tripThumbnailUrl"
    }
}