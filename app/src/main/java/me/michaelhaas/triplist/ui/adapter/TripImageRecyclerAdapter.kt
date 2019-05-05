package me.michaelhaas.triplist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import me.michaelhaas.triplist.R
import me.michaelhaas.triplist.service.core.model.resolver.ImageResolver

class TripImageRecyclerAdapter(
    private val items: List<ImageResolver>
) : RecyclerView.Adapter<TripImageRecyclerAdapter.TripImageViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripImageViewHolder =
        TripImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_image, parent, false))

    override fun onBindViewHolder(holder: TripImageViewHolder, position: Int) {
        items.getOrNull(position)?.resolveInto(holder.imageView, maxHeight = 800, usePlaceholder = true)
    }

    class TripImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.trip_detail_image)
    }
}