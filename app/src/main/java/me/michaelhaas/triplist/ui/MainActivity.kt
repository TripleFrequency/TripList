package me.michaelhaas.triplist.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.ui.adapter.TripPagerAdapter

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    fun scrollToAllTrips() {
        content_pager?.let {
            it.currentItem = TripPagerAdapter.TAB_POSITION_ALL_TRIPS
        }
    }
}
