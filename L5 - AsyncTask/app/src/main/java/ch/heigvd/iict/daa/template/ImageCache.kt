package ch.heigvd.iict.daa.template

import android.graphics.Bitmap
import androidx.collection.LruCache

object ImageCache {
    // Taille maximale du cache en mémoire (en fonction de la mémoire disponible)
    private val cache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(
        (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt() // 1/8 de la mémoire
    ) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024 // En kilooctets
        }
    }

    // Ajouter une image au cache
    fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    // Récupérer une image du cache
    fun get(key: String): Bitmap? {
        return cache.get(key)?.also {
            println("Image récupérée depuis le cache : $key")
        } ?: run {
            println("Image absente du cache (à télécharger) : $key")
            null
        }
    }

    fun clear() {
        cache.evictAll()
    }
}