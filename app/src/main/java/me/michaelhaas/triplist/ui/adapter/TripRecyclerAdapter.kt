package me.michaelhaas.triplist.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.service.core.model.UserTrip
import java.util.*

class TripRecyclerAdapter<T : Any>(items: List<T>? = null) :
    RecyclerView.Adapter<TripRecyclerAdapter.TripViewHolder<T>>() {

    var items: List<T> = items ?: emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int = when (items.getOrNull(position)) {
        is UserTrip -> VIEW_TYPE_USER_TRIP
        is Trip -> VIEW_TYPE_TRIP
        else -> VIEW_TYPE_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return when (viewType) {
            VIEW_TYPE_USER_TRIP -> TripViewHolder.UserTripViewHolder(view) as TripViewHolder<T>
            VIEW_TYPE_TRIP -> TripViewHolder.GeneralTripViewHolder(view) as TripViewHolder<T>
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: TripViewHolder<T>, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    sealed class TripViewHolder<T : Any>(protected val view: View) : RecyclerView.ViewHolder(view) {

        protected val thumbnailView: ImageView = view.findViewById(R.id.trip_thumbnail)
        protected val titleView: TextView = view.findViewById(R.id.trip_title)
        protected val subLineView: TextView = view.findViewById(R.id.trip_sub_line)

        abstract fun bind(item: T)

        class UserTripViewHolder(view: View) : TripViewHolder<UserTrip>(view) {
            override fun bind(item: UserTrip) {
                titleView.text = item.trip.name
                subLineView.text = formatDates(item.startDate, item.endDate) ?: ""
                item.trip.thumbnail.resolveInto(thumbnailView)
            }

            private fun formatDates(startDate: Date, endDate: Date) = view.context?.let {
                DateUtils.formatDateRange(it, startDate.time, endDate.time, DateUtils.FORMAT_ABBREV_RELATIVE)
            }
        }

        class GeneralTripViewHolder(view: View) : TripViewHolder<Trip>(view) {
            override fun bind(item: Trip) {
                titleView.text = item.name
                subLineView.visibility = View.GONE
                item.thumbnail.resolveInto(thumbnailView)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0

        const val VIEW_TYPE_USER_TRIP = 1
        const val VIEW_TYPE_TRIP = 2
    }
}