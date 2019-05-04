package me.michaelhaas.triplist.service.core.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64() = Base64.encodeToString(ByteArrayOutputStream().also { baos ->
    compress(Bitmap.CompressFormat.PNG, 100, baos)
}.toByteArray(), Base64.DEFAULT)