package me.michaelhaas.triplist.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjection
import me.michaelhaas.triplist.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_container, UserTripsFragment.Builder().build())
            .addToBackStack(UserTripsFragment.NAVIGATION_NAME)
            .commit()
    }
}
