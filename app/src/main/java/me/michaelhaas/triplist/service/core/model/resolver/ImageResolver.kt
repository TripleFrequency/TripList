package me.michaelhaas.triplist.service.core.model.resolver

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

open class ImageResolver(private val url: Uri) : BaseResolver<Bitmap>() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun resolveInto(view: ImageView) = scope.launch {
        val bitmap = withContext(Dispatchers.IO) { resolve() }
        view.setImageBitmap(bitmap)
    }

    override suspend fun resolve(): Bitmap? = Picasso.get().load(url).get()
}