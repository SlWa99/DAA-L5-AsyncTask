package ch.heigvd.iict.daa.template

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

// Téléchargement de l'image
suspend fun downloadImage(url: URL): ByteArray? = withContext(Dispatchers.IO) {
    try {
        url.readBytes()
    } catch (e: IOException) {
        Log.w("ImageDownload", "Exception while downloading image", e)
        null
    }
}

// Décodage de l'image
suspend fun decodeImage(bytes: ByteArray?): Bitmap? = withContext(Dispatchers.Default) {
    try {
        bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    } catch (e: IOException) {
        Log.w("ImageDecode", "Exception while decoding image", e)
        null
    }
}

// Affichage de l'image
suspend fun displayImage(imageView: ImageView, bmp: Bitmap?) = withContext(Dispatchers.Main) {
    if (bmp != null) {
        imageView.setImageBitmap(bmp)
    } else {
        imageView.setImageResource(R.drawable.error_placeholder)
    }
}