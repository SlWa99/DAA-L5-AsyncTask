package ch.heigvd.iict.daa.template

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

// Téléchargement de l'image
suspend fun downloadImage(url: URL): ByteArray? = withContext(Dispatchers.IO) {
    try {
        Log.d("ImageDownload", "Téléchargement de : $url")
        val bytes = url.readBytes()
        Log.d("ImageDownload", "Téléchargement réussi : ${bytes.size} bytes")
        bytes
    } catch (e: IOException) {
        Log.e("ImageDownload", "Erreur lors du téléchargement", e)
        null
    }
}

// Décodage de l'image
suspend fun decodeImage(bytes: ByteArray?): Bitmap? = withContext(Dispatchers.Default) {
    try {
        if (bytes == null) {
            Log.e("ImageDecode", "Les bytes sont nulls")
            return@withContext null
        }
        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        Log.d("ImageDecode", "Décodage réussi : ${bmp.width}x${bmp.height}")
        bmp
    } catch (e: Exception) {
        Log.e("ImageDecode", "Erreur de décodage", e)
        null
    }
}

// Affichage de l'image
suspend fun displayImage(imageView: ImageView, bmp: Bitmap?) = withContext(Dispatchers.Main) {
    if (bmp != null) {
        imageView.setImageBitmap(bmp)
        Log.d("displayImage", "display réussi : ${bmp.width}x${bmp.height}")
    } else {
        imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        Log.d("displayImage", "display échoué !")
    }
}