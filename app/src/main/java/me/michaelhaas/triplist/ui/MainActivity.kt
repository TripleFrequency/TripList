package me.michaelhaas.triplist.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.ui.adapter.TripPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        tabs_trips?.setupWithViewPager(content_pager)
        content_pager?.adapter = TripPagerAdapter(this, supportFragmentManager)
    }

    override fun onBackPressed() {
        if (content_pager?.currentItem ?: 0 == 0) {
            super.onBackPressed()
        } else {
            content_pager?.let {
                it.currentItem--
            }
        }
    }

    fun openDetailsFromFragment(fragment: Fragment, trip: Trip, userTripId: Int?, sharedViews: Array<Pair<View, String>>) {
        val (intent, bundle) = TripDetailsActivity.Builder(
            trip.id,
            userTripId,
            trip.thumbnail.url.toString(),
            true,
            *sharedViews
        ).buildIntent(this)
        if (bundle == null) {
            startActivity(intent)
        } else {
            startActivityFromFragment(fragment, intent, 0, bundle)
        }
    }

    fun scrollToAllTrips() {
        content_pager?.let {
            it.currentItem = TripPagerAdapter.TAB_POSITION_ALL_TRIPS
        }
    }
}
