package me.michaelhaas.triplist.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_trip_editor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.analytics.AnalyticsBuilder
import me.michaelhaas.triplist.ui.vm.TripEditorViewModel
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class TripEditorFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var tripId: Int = 0

    private var userTripId: Int? = null
    private var startDate: Date? = null
    private var endDate: Date? = null

    private var newStartDate: Date? = null
    private var newEndDate: Date? = null

    @Inject
    lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val editorViewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        )[TripEditorViewModel::class.java]
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_trip_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tripId = arguments?.getInt(ARG_TRIP_ID, 0) ?: 0
        userTripId = arguments?.getInt(ARG_USER_TRIP_ID)?.let { if (it == 0) null else it }
        startDate = arguments?.getLong(ARG_START_DATE)?.let { if (it == 0L) null else it }?.let { Date(it) }
        endDate = arguments?.getLong(ARG_END_DATE)?.let { if (it == 0L) null else it }?.let { Date(it) }

        if (tripId == 0) {
            throw IllegalStateException("No Trip Id provided to editor fragment")
        }

        displayDateIn(startDate, trip_start_date)
        newStartDate = startDate
        displayDateIn(endDate, trip_end_date)
        newEndDate = endDate

        trip_start_date?.setOnClickListener { editText ->
            editText.isClickable = false
            showPicker(startDate, ARG_START_DATE)
        }
        trip_end_date?.setOnClickListener { editText ->
            editText.isClickable = false
            showPicker(endDate, ARG_END_DATE)
        }

        if (userTripId != null) {
            trip_delete_button?.visibility = View.VISIBLE
        }

        trip_delete_button?.setOnClickListener { buttonView ->
            userTripId?.let {
                AlertDialog.Builder(buttonView.context)
                    .setTitle(R.string.dialog_delete_title)
                    .setMessage(R.string.dialog_delete_message)
                    .setIcon(R.drawable.ic_delete_forever_24dp)
                    .setPositiveButton(R.string.dialog_delete_title) { dialog, _ ->
                        editorViewModel.deleteUserTrip(it, tripId)
                        dialog.dismiss()
                        activity?.supportFinishAfterTransition()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                        AnalyticsBuilder.TripEditorEvent.TripEditorDeleteCancelledEvent(tripId).log(analytics)
                    }.show()
            }
        }

        trip_save_button?.setOnClickListener {
            val nStartDate = newStartDate
            val nEndDate = newEndDate
            if (nStartDate == null || nEndDate == null) {
                if (nStartDate == null) {
                    trip_start_date?.error = ""
                }
                if (nEndDate == null) {
                    trip_end_date?.error = ""
                }
            } else if (nEndDate.before(nStartDate)) {
                trip_start_date?.error = getString(R.string.error_trip_end_before_start)
                trip_end_date?.error = getString(R.string.error_trip_start_after_end)
            } else {
                editorViewModel.launch {
                    trip_save_button?.isEnabled = false
                    var newId: Int? = null
                    withContext(Dispatchers.IO) {
                        val uTripId = userTripId
                        if (uTripId != null) {
                            editorViewModel.updateUserTrip(uTripId, tripId, nStartDate, nEndDate)
                        } else {
                            val inserted = editorViewModel.insertUserTrip(tripId, nStartDate, nEndDate).toInt()
                            newId = inserted
                        }
                    }
                    (activity as? TripDetailsActivity?)?.onEditorClosed(newId)
                    fragmentManager?.popBackStack()
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
        if (view?.tag == ARG_START_DATE) {
            newStartDate = calendar.time
            trip_start_date?.isClickable = true
            trip_start_date?.error = null
            trip_start_date?.setText(dateString)
        } else {
            newEndDate = calendar.time
            trip_end_date?.isClickable = true
            trip_end_date?.error = null
            trip_end_date?.setText(dateString)
        }
    }

    private fun displayDateIn(date: Date?, editText: EditText?) {
        date?.let {
            editText?.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(it))
        }
    }

    private fun showPicker(date: Date?, tag: String) {
        context?.let { context ->
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()
            val picker = DatePickerDialog(
                context,
                this,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            picker.datePicker.tag = tag
            picker.show()
        }
    }

    class Builder(
        var tripId: Int,
        var userTripId: Int? = null,
        var tripStart: Date? = null,
        var tripEnd: Date? = null
    ) {
        fun build() = TripEditorFragment().apply {
            arguments = Bundle().also { bundle ->
                bundle.putInt(ARG_TRIP_ID, this@Builder.tripId)
                this@Builder.userTripId?.let { bundle.putInt(ARG_USER_TRIP_ID, it) }
                tripStart?.let { bundle.putLong(ARG_START_DATE, it.time) }
                tripEnd?.let { bundle.putLong(ARG_END_DATE, it.time) }
            }
        }
    }

    companion object {
        private const val ARG_TRIP_ID = "tripId"
        private const val ARG_USER_TRIP_ID = "userTripId"
        private const val ARG_START_DATE = "startDate"
        private const val ARG_END_DATE = "endDate"

        const val BACK_NAVIGATION_TAG = "tripEditor"
    }
}