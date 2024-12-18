/**
 * Nom du fichier : ImageCache.kt
 * Description    : Gestionnaire de cache pour les images, utilisant un LruCache pour optimiser
 *                  l'utilisation de la mémoire et améliorer les performances de chargement.
 * Auteurs        : Bugna, Slimani & Steiner
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache

/**
 * Objet : ImageCache
 * Description : Gère un cache en mémoire pour stocker et récupérer des images afin de réduire
 *               les téléchargements redondants et d'améliorer les performances de l'application.
 */
object ImageCache {
    // Taille maximale du cache en mémoire (en fonction de la mémoire disponible)
    private var cache: LruCache<String, Bitmap>? = null
    /**
     * Initialiser le cache avec le contexte
     * Cette méthode est appelée dans `MainActivity.kt`
     */
    fun initialize(context: Context) {
        if (cache == null) {
            val kiloOctets = context.resources.getString(R.string.kilo_octets).toInt()
            val octets = context.resources.getString(R.string.octets).toInt()
            cache = object : LruCache<String, Bitmap>(
                // Calcule la taille du cache basée sur les valeurs dans integer.xml
                (Runtime.getRuntime().maxMemory() / kiloOctets / octets).toInt() // 1/8 de la mémoire
            ) {
                /**
                 * Méthode : sizeOf
                 * Description : Calcule la taille d'une image dans le cache, en kilo octets.
                 * @param key Clé de l'image (URL ou identifiant).
                 * @param value Bitmap associé.
                 * @return Taille en kilooctets.
                 */
                override fun sizeOf(key: String, value: Bitmap): Int {
                    return value.byteCount / kiloOctets // En kilo octets
                }
            }
        }
    }

    /**
     * Méthode : put
     * Description : Ajoute une image au cache.
     * @param key Clé associée à l'image (généralement son URL).
     * @param bitmap Bitmap de l'image à stocker.
     */
    fun put(key: String, bitmap: Bitmap) {
        cache?.put(key, bitmap)
    }

    /**
     * Méthode : get
     * Description : Récupère une image du cache si elle y est présente.
     * @param key Clé associée à l'image.
     * @return Bitmap de l'image ou null si absente.
     */
    fun get(key: String): Bitmap? {
        return cache?.get(key)?.also {
            println("Image récupérée depuis le cache : $key")
        } ?: run {
            println("Image absente du cache (à télécharger) : $key")
            null
        }
    }

    /**
     * Méthode : clear
     * Description : Vide complètement le cache des images.
     */
    fun clear() {
        cache?.evictAll()
    }
}