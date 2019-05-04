package me.michaelhaas.triplist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_trips.*
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
    ): View? = inflater.inflate(R.layout.fragment_trips, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        no_trips_icon?.setBackgroundResource(R.drawable.ic_card_travel_black_24dp)

        no_trips_text?.text = getString(R.string.label_trip_none_planned)

        view_all_trips_button?.visibility = View.VISIBLE
        view_all_trips_button?.text = getString(R.string.button_all_trips)
        view_all_trips_button?.setOnClickListener { viewAllClicked() }

        userTripsViewModel.userTripLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                showZeroState()
            } else {
                hideZeroState()
            }
        })
    }

    private fun showZeroState() {
        user_trip_recycler?.visibility = View.GONE
        container_trips_zero_state?.visibility = View.VISIBLE
    }

    private fun hideZeroState() {
        user_trip_recycler?.visibility = View.VISIBLE
        container_trips_zero_state?.visibility = View.GONE
    }

    private fun viewAllClicked() {
        (activity as? MainActivity?)?.scrollToAllTrips()
    }

    class Builder {
        fun build() = UserTripsFragment()
    }
}