/**
 * Nom du fichier : ImageUtils.kt
 * Description    : Fonctions utilitaires pour le téléchargement, le décodage et
 *                  l'affichage des images.
 * Auteurs        : ICI
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

/**
 * Fonction : downloadImage
 * Description : Télécharge une image depuis une URL sous forme de tableau de bytes.
 * @param url URL de l'image à télécharger.
 * @return Tableau de bytes contenant les données de l'image ou null en cas d'échec.
 */
suspend fun downloadImage(url: URL): ByteArray? = withContext(Dispatchers.IO) {
    try {
        Log.d("ImageDownload", "Téléchargement de : $url")
        val bytes = url.readBytes()
        Log.d("ImageDownload", "Téléchargement réussi : ${bytes.size} bytes")
        bytes
    } catch (e: IOException) {
        Log.e("ImageDownload", "Erreur lors du téléchargement", e)
        null
    } catch (e: Exception) {
        Log.e("ImageDownload", "Erreur inattendue", e)
        null
    }
}

/**
 * Fonction : decodeImage
 * Description : Décode un tableau de bytes en un Bitmap.
 * @param bytes Tableau de bytes représentant l'image.
 * @return Bitmap décodé ou null si le décodage échoue.
 */
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

/**
 * Fonction : displayImage
 * Description : Affiche un Bitmap dans un ImageView sur le thread principal.
 *               Si le Bitmap est null, affiche une icône par défaut.
 * @param imageView ImageView où afficher l'image.
 * @param bmp Bitmap à afficher.
 */
suspend fun displayImage(imageView: ImageView, bmp: Bitmap?) = withContext(Dispatchers.Main) {
    if (bmp != null) {
        imageView.setImageBitmap(bmp)
        Log.d("displayImage", "display réussi : ${bmp.width}x${bmp.height}")
    } else {
        imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        Log.d("displayImage", "display échoué !")
    }
}