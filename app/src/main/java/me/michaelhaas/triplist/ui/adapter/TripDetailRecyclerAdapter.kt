package me.michaelhaas.triplist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.TripDetails

class TripDetailRecyclerAdapter(
    private val details: TripDetails
) : RecyclerView.Adapter<TripDetailRecyclerAdapter.TripDetailViewHolder>() {

    override fun getItemCount() = NON_ACTIVITY_LIST_COUNT + details.activities.size

    override fun getItemViewType(position: Int): Int =
        if (position % 2 == 0 && position < NON_ACTIVITY_LIST_COUNT) {
            VIEW_TYPE_HEADER
        } else if (position < NON_ACTIVITY_LIST_COUNT) {
            VIEW_TYPE_IMAGE_LIST
        } else {
            VIEW_TYPE_ACTIVITY_ITEM
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDetailViewHolder = when (viewType) {
        VIEW_TYPE_HEADER -> LayoutInflater.from(parent.context).inflate(
            R.layout.item_detail_header,
            parent,
            false
        ).let { TripDetailViewHolder.HeaderViewHolder(it) }
        VIEW_TYPE_IMAGE_LIST -> LayoutInflater.from(parent.context).inflate(
            R.layout.item_detail_image_list,
            parent,
            false
        ).let { TripDetailViewHolder.ImageListViewHolder(it) }
        VIEW_TYPE_ACTIVITY_ITEM -> LayoutInflater.from(parent.context).inflate(
            R.layout.item_detail_activity,
            parent,
            false
        ).let { TripDetailViewHolder.ActivityItemViewHolder(it) }
        else -> throw IllegalStateException("Attempt to use invalid view type: $viewType")
    }

    override fun onBindViewHolder(holder: TripDetailViewHolder, position: Int) {
        when (holder) {
            is TripDetailViewHolder.HeaderViewHolder -> {
                holder.headerView.text = holder.headerView.context.getString(
                    when (position) {
                        POSITION_HEADER_PHOTOS -> R.string.detail_header_photos
                        POSITION_HEADER_ACTIVITIES -> R.string.detail_header_activities
                        else -> throw IllegalStateException("Invalid header position $position")
                    }
                )
            }
            is TripDetailViewHolder.ImageListViewHolder -> {
                holder.imageRecycler.layoutManager =
                    LinearLayoutManager(holder.imageRecycler.context, RecyclerView.HORIZONTAL, false)
                holder.imageRecycler.adapter = TripImageRecyclerAdapter(details.photos)
            }
            is TripDetailViewHolder.ActivityItemViewHolder -> {
                details.activities.getOrNull(position - NON_ACTIVITY_LIST_COUNT)?.let {
                    holder.titleView.text = it.name
                    holder.descriptionView.text = it.description
                }
            }
        }
    }

    sealed class TripDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class HeaderViewHolder(view: View) : TripDetailViewHolder(view) {
            val headerView: TextView = view.findViewById(R.id.trip_detail_header)
        }

        class ImageListViewHolder(view: View) : TripDetailViewHolder(view) {
            val imageRecycler: RecyclerView = view.findViewById(R.id.trip_detail_image_recycler)
        }

        class ActivityItemViewHolder(view: View) : TripDetailViewHolder(view) {
            val titleView: TextView = view.findViewById(R.id.trip_activity_title)
            val descriptionView: TextView = view.findViewById(R.id.trip_activity_description)
        }
    }

    companion object {
        private const val SECTION_COUNT = 2
        private const val NON_ACTIVITY_LIST_COUNT = SECTION_COUNT + 1

        private const val POSITION_HEADER_PHOTOS = 0
        private const val POSITION_HEADER_ACTIVITIES = 2

        private const val VIEW_TYPE_HEADER = 1
        private const val VIEW_TYPE_IMAGE_LIST = 2
        private const val VIEW_TYPE_ACTIVITY_ITEM = 3
    }
}