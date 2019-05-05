package me.michaelhaas.triplist.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_trip_editor.*
import me.michaelhaas.triplist.R
import java.text.DateFormat
import java.util.*

class TripEditorFragment : Fragment() {

    private var userTripId: Int? = null
    private var startDate: Long? = null
    private var endDate: Long? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_trip_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userTripId = arguments?.getInt(ARG_USER_TRIP_ID)?.let { if (it == 0) null else it }
        startDate = arguments?.getLong(ARG_START_DATE)?.let { if (it == 0L) null else it } ?: 0
        endDate = arguments?.getLong(ARG_END_DATE)?.let { if (it == 0L) null else it }

        startDate?.let {
            trip_start_date?.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(it)))
        }
        endDate?.let {
            trip_end_date?.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(it)))
        }

        trip_save_button?.setOnClickListener {
            // Validate and show errors

            // If valid, save then close editor
            (activity as? TripDetailsActivity?)?.onEditorClosed(0)
            fragmentManager?.popBackStack()
        }
    }

    class Builder(
        var userTripId: Int? = null,
        var tripStart: Date? = null,
        var tripEnd: Date? = null,
        var onSave: (() -> Unit)? = null
    ) {
        fun build() = TripEditorFragment().apply {
            arguments = Bundle().also { bundle ->
                userTripId?.let { bundle.putInt(ARG_USER_TRIP_ID, it) }
                tripStart?.let { bundle.putLong(ARG_START_DATE, it.time) }
                tripEnd?.let { bundle.putLong(ARG_END_DATE, it.time) }
            }
        }
    }

    companion object {
        private const val ARG_USER_TRIP_ID = "userTripId"
        private const val ARG_START_DATE = "startDate"
        private const val ARG_END_DATE = "endDate"

        const val BACK_NAVIGATION_TAG = "tripEditor"
    }
}