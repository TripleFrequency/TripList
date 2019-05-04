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
import me.michaelhaas.triplist.ui.vm.AllTripsViewModel
import javax.inject.Inject

class AllTripsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val allTripsViewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        )[AllTripsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trips, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        no_trips_icon?.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)

        no_trips_text?.text = getString(R.string.label_no_trips_available)

        view_all_trips_button?.visibility = View.GONE

        allTripsViewModel.allTripsLiveData.observe(this, Observer {
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

    class Builder {
        fun build() = AllTripsFragment()
    }
}