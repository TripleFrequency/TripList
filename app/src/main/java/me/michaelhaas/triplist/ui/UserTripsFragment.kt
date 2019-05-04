package me.michaelhaas.triplist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_trips.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.UserTrip
import me.michaelhaas.triplist.ui.adapter.TripRecyclerAdapter
import me.michaelhaas.triplist.ui.vm.UserTripsViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserTripsFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val userTripsViewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        )[UserTripsViewModel::class.java]
    }

    private val tripRecyclerAdapter by lazy {
        TripRecyclerAdapter<UserTrip>({ trip, sharedViews ->
            (activity as? MainActivity?)?.openDetailsFromFragment(this, trip, sharedViews)
        })
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

        refresh_trip?.setOnRefreshListener(this)

        context?.let {
            user_trip_recycler?.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            user_trip_recycler?.adapter = tripRecyclerAdapter
        }

        userTripsViewModel.userTripLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                showZeroState()
            } else {
                tripRecyclerAdapter.items = it
                hideZeroState()
            }
        })
    }

    override fun onRefresh() {
        userTripsViewModel.launch {
            // "Your Trips" is entirely reactive, so there isn't a way to "refresh" per-se.
            // As a result, visual feedback is provided, even though it does nothing.
            delay(100)
            refresh_trip?.isRefreshing = false
        }
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