package me.michaelhaas.triplist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_trips.*
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.ui.vm.UserTripsViewModel
import javax.inject.Inject

class UserTripsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val userTripsViewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        )[UserTripsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user_trips, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container_trips_zero_state?.setOnClickListener { viewAllClicked() }
        userTripsViewModel.userTripLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                user_trip_recycler?.visibility = View.GONE
                container_trips_zero_state?.visibility = View.VISIBLE
            } else {
                user_trip_recycler?.visibility = View.VISIBLE
                container_trips_zero_state?.visibility = View.GONE
            }
        })
    }

    private fun viewAllClicked() {
        // TODO Switch tab to all trips
    }

    class Builder {
        fun build() = UserTripsFragment()
    }

    companion object {
        const val NAVIGATION_NAME = "FRAGMENT_USER_TRIPS"
    }
}