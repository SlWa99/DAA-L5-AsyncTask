/**
 * Nom du fichier : Adapter.kt
 * Description    : Adapter pour la RecyclerView, gérant l'affichage des images numérotées
 *                  avec gestion du cache et téléchargement asynchrone.
 * Auteurs        : ICI
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Classe : Adapter
 * Description : Adapter pour la RecyclerView, gérant une liste de 10 000 URLs d'images.
 *               Les images sont chargées à la demande, avec une gestion efficace du cache
 *               pour optimiser les performances.
 * @param lifecycleOwner Référence au LifecycleOwner pour utiliser
 *                       les coroutines dans le cycle de vie.
 */
class Adapter(private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<Adapter.ImageViewHolder>() {

    // Liste des URLs des images
    private val imageUrls = (1..10000).map { "https://daa.iict.ch/images/$it.jpg" }

    /**
     * Méthode : onCreateViewHolder
     * Description : Crée une nouvelle instance de ViewHolder pour un élément de la RecyclerView.
     * @param parent Vue parente.
     * @param viewType Type de la vue (non utilisé ici).
     * @return Une instance d'ImageViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    /**
     * Méthode : onBindViewHolder
     * Description : Lie un URL d'image à un ViewHolder spécifique pour l'affichage.
     * @param holder Instance de ImageViewHolder.
     * @param position Position de l'élément dans la liste.
     */
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.bind(imageUrl)
    }

    /**
     * Méthode : getItemCount
     * Description : Retourne le nombre total d'éléments dans la liste.
     * @return Nombre total d'éléments.
     */
    override fun getItemCount(): Int = imageUrls.size

    /**
     * Classe : ImageViewHolder
     * Description : ViewHolder personnalisé pour gérer l'affichage d'une image
     *               et le contrôle de son téléchargement ou récupération dans le cache.
     */
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: View = itemView.findViewById(R.id.progressBar)

        /**
         * Méthode : bind
         * Description : Configure l'ImageView avec l'image correspondante (cache ou téléchargement).
         * @param imageUrl URL de l'image à afficher.
         */
        fun bind(imageUrl: String) {
            // Affiche la ProgressBar
            progressBar.visibility = View.VISIBLE
            imageView.setImageDrawable(null) // Réinitialise l'image

            // Vérifie si l'image est déjà en cache
            val cachedBitmap = ImageCache.get(imageUrl)
            if (cachedBitmap != null) {
                // Si l'image est en cache, l'affiche directement
                Log.d("ImageCache", "Image récupérée depuis le cache : $imageUrl")
                imageView.setImageBitmap(cachedBitmap)
                progressBar.visibility = View.INVISIBLE // chelou jsp pk le gone marche pas
            } else {
                // Sinon, télécharge et décode l'image
                lifecycleOwner.lifecycleScope.launch {
                    try {
                        val bytes = downloadImage(URL(imageUrl))
                        val bmp = decodeImage(bytes)

                        if (bmp != null) {
                            // Ajoute l'image au cache
                            ImageCache.put(imageUrl, bmp)
                            Log.d("ImageCache", "Image téléchargée et ajoutée au cache : $imageUrl")
                            displayImage(imageView, bmp)
                        } else {
                            // En cas d'échec de décodage
                            imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                        }
                    } catch (e: Exception) {
                        // En cas d'erreur de téléchargement
                        Log.e("ImageDownload", "Erreur lors du téléchargement", e)
                        imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                    } finally {
                        // Masque la ProgressBar après le traitement
                        progressBar.visibility = View.INVISIBLE // chelou jsp pk le gone marche pas
                    }
                }
            }
        }
    }
}