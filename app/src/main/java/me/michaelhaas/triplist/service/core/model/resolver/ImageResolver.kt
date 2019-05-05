package me.michaelhaas.triplist.service.core.model.resolver

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import me.michaelhaas.triplist.R

open class ImageResolver(val url: Uri) : BaseResolver<Bitmap>() {

    fun resolveInto(view: ImageView, maxWidth: Int? = null, maxHeight: Int? = null, usePlaceholder: Boolean = false) =
        Picasso.get().load(url).let {
            if (usePlaceholder) {
                it.placeholder(ColorDrawable(view.context.getColor(android.R.color.darker_gray)))
            }
            if (maxWidth != null || maxHeight != null) {
                it.resize(maxWidth ?: 0, maxHeight ?: 0)
            } else {
                it
            }
        }.into(view)

    override suspend fun resolve(): Bitmap? = Picasso.get().load(url).get()
}