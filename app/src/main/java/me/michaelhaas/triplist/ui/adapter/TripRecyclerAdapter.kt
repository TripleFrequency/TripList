package me.michaelhaas.triplist.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.Trip
import me.michaelhaas.triplist.service.core.model.UserTrip
import java.util.*

class TripRecyclerAdapter<T : Any>(
    private val tripClickListener: ((Trip, Int?, Array<Pair<View, String>>) -> Unit)? = null,
    items: List<T>? = null
) :
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
            VIEW_TYPE_USER_TRIP -> TripViewHolder.UserTripViewHolder(view, tripClickListener) as TripViewHolder<T>
            VIEW_TYPE_TRIP -> TripViewHolder.GeneralTripViewHolder(view, tripClickListener) as TripViewHolder<T>
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: TripViewHolder<T>, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    sealed class TripViewHolder<T : Any>(
        protected val view: View,
        protected val clickListener: ((Trip, Int?, Array<Pair<View, String>>) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {

        protected val gradientView: ImageView = view.findViewById(R.id.text_gradient)
        protected val containerView: View = view.findViewById(R.id.trip_container)
        protected val thumbnailView: ImageView = view.findViewById(R.id.trip_thumbnail)
        protected val titleView: TextView = view.findViewById(R.id.trip_title)
        protected val subLineView: TextView = view.findViewById(R.id.trip_sub_line)

        abstract fun bind(item: T)

        class UserTripViewHolder(
            view: View,
            clickListener: ((Trip, Int?, Array<Pair<View, String>>) -> Unit)?
        ) : TripViewHolder<UserTrip>(view, clickListener) {
            override fun bind(item: UserTrip) {
                containerView.setOnClickListener {
                    val resources = it?.context?.resources

                    val imageTransitionName = resources?.getString(R.string.transition_trip_image)
                    val gradientTransitionName = resources?.getString(R.string.transition_trip_gradient)
                    val titleTransitionName = resources?.getString(R.string.transition_trip_title)
                    val subLineTransitionName = resources?.getString(R.string.transition_trip_sub_line)

                    if (imageTransitionName != null && gradientTransitionName != null && titleTransitionName != null && subLineTransitionName != null) {
                        clickListener?.invoke(
                            item.trip,
                            item.id,
                            arrayOf<Pair<View, String>>(
                                Pair(thumbnailView, imageTransitionName),
                                Pair(gradientView, gradientTransitionName),
                                Pair(titleView, titleTransitionName),
                                Pair(subLineView, subLineTransitionName)
                            )
                        )
                    }
                }
                titleView.text = item.trip.name
                subLineView.text = formatDates(item.startDate, item.endDate) ?: ""
                item.trip.thumbnail.resolveInto(thumbnailView)
            }

            private fun formatDates(startDate: Date, endDate: Date) = view.context?.let {
                DateUtils.formatDateRange(it, startDate.time, endDate.time, DateUtils.FORMAT_ABBREV_RELATIVE)
            }

            companion object {
                private const val SPINNER_POSITION_DELETE = 0
            }
        }

        class GeneralTripViewHolder(
            view: View,
            clickListener: ((Trip, Int?, Array<Pair<View, String>>) -> Unit)?
        ) : TripViewHolder<Trip>(view, clickListener) {
            override fun bind(item: Trip) {
                containerView.setOnClickListener {
                    val resources = it?.context?.resources

                    val imageTransitionName = resources?.getString(R.string.transition_trip_image)
                    val gradientTransitionName = resources?.getString(R.string.transition_trip_gradient)
                    val titleTransitionName = resources?.getString(R.string.transition_trip_title)

                    if (imageTransitionName != null && gradientTransitionName != null && titleTransitionName != null) {
                        clickListener?.invoke(
                            item,
                            null,
                            arrayOf<Pair<View, String>>(
                                Pair(thumbnailView, imageTransitionName),
                                Pair(gradientView, gradientTransitionName),
                                Pair(titleView, titleTransitionName)
                            )
                        )
                    }
                }
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