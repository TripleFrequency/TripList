package me.michaelhaas.triplist.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_trip_editor.*
import me.michaelhaas.triplist.R
import java.text.DateFormat
import java.util.*

class TripEditorFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var userTripId: Int? = null
    private var startDate: Date? = null
    private var endDate: Date? = null

    private var newStartDate: Date? = null
    private var newEndDate: Date? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_trip_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userTripId = arguments?.getInt(ARG_USER_TRIP_ID)?.let { if (it == 0) null else it }
        startDate = arguments?.getLong(ARG_START_DATE)?.let { if (it == 0L) null else it }?.let { Date(it) }
        endDate = arguments?.getLong(ARG_END_DATE)?.let { if (it == 0L) null else it }?.let { Date(it) }

        displayDateIn(startDate, trip_start_date)
        displayDateIn(endDate, trip_end_date)

        trip_start_date?.setOnClickListener { _ -> showPicker(startDate, ARG_START_DATE) }
        trip_end_date?.setOnClickListener { _ -> showPicker(endDate, ARG_END_DATE) }

        trip_save_button?.setOnClickListener {
            if (newStartDate == null || newEndDate == null) {
                if (newStartDate == null) {
                    trip_start_date?.error = ""
                }
                if (newEndDate == null) {
                    trip_end_date?.error = ""
                }
            } else {
                (activity as? TripDetailsActivity?)?.onEditorClosed(if (userTripId == null) 0 else null)
                fragmentManager?.popBackStack()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
        if (view?.tag == ARG_START_DATE) {
            newStartDate = calendar.time
            trip_start_date?.error = null
            trip_start_date?.setText(dateString)
        } else {
            newEndDate = calendar.time
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
        var userTripId: Int? = null,
        var tripStart: Date? = null,
        var tripEnd: Date? = null
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