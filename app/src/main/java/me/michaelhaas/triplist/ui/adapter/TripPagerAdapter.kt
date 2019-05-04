package me.michaelhaas.triplist.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.ui.AllTripsFragment
import me.michaelhaas.triplist.ui.UserTripsFragment

class TripPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = TAB_COUNT

    override fun getItem(position: Int) = when (position) {
        TAB_POSITION_YOUR_TRIPS -> UserTripsFragment.Builder().build()
        TAB_POSITION_ALL_TRIPS -> AllTripsFragment.Builder().build()
        else -> throw IllegalStateException("Attempt to open non-existent tab at index $position")
    } as Fragment

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        TAB_POSITION_YOUR_TRIPS -> context.resources.getString(R.string.label_your_trips)
        TAB_POSITION_ALL_TRIPS -> context.resources.getString(R.string.label_all_trips)
        else -> throw IllegalStateException("Attempt to retrieve title for non-existent tab at index $position")
    }

    companion object {
        const val TAB_POSITION_YOUR_TRIPS = 0
        const val TAB_POSITION_ALL_TRIPS = 1

        const val TAB_COUNT = 2
    }
}