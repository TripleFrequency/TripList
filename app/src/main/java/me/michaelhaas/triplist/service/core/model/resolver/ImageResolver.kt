package me.michaelhaas.triplist.service.core.model.resolver

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso

open class ImageResolver(val url: Uri) : BaseResolver<Bitmap>() {

    fun resolveInto(view: ImageView) = Picasso.get().load(url).into(view)

    override suspend fun resolve(): Bitmap? = Picasso.get().load(url).get()
}