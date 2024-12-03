/**
 * Nom du fichier : Adapter.kt
 * Description    : Adapter pour la RecyclerView, gérant l'affichage des images numérotées
 *                  avec gestion du cache et téléchargement asynchrone.
 * Auteurs        : Bugna, Slimani & Steiner
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
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
     * Méthode : onViewRecycled
     * Description : Appelée lorsqu'une vue associée à un ViewHolder est recyclée. Cette méthode
     *               garantit que toutes les Coroutines associées au ViewHolder sont annulées,
     *               libérant ainsi les ressources et évitant les tâches inutiles.
     * @param holder Instance de ImageViewHolder dont la vue est recyclée.
     */
    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelJob() // Annule la Coroutine associée à cette vue recyclée
    }

    /**
     * Classe : ImageViewHolder
     * Description : ViewHolder personnalisé pour gérer l'affichage d'une image
     *               et le contrôle de son téléchargement ou récupération dans le cache.
     */
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: View = itemView.findViewById(R.id.progressBar)

        // Job pour gérer les Coroutines associées à ce ViewHolder
        private var job: Job? = null

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
                Log.d("ImageCache", "Image récupérée depuis le cache : $imageUrl")
                // Affiche l'image directement depuis le cache
                imageView.setImageBitmap(cachedBitmap)
                progressBar.visibility = View.INVISIBLE
            } else {
                job?.cancel()
                // Crée un nouveau Job pour cette vue
                job = lifecycleOwner.lifecycleScope.launch {
                    try {
                        // Télécharge et décode l'image
                        val bytes = downloadImage(URL(imageUrl))
                        val bmp = decodeImage(bytes)

                        if (bmp != null) {
                            ImageCache.put(imageUrl, bmp)
                            Log.d("ImageCache", "Image téléchargée et ajoutée au cache : $imageUrl")
                            displayImage(imageView, bmp)
                        } else {
                            Log.d("ImageDownload", "Erreur lors du décodage, image par défaut affichée")
                            imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                        }
                    } catch (e: Exception) {
                        Log.e("ImageDownload", "Erreur lors du téléchargement", e)
                        imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                    } finally {
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }

            // Gestion du clique sur une image TEST
            imageView.setOnClickListener {
                Log.d("Click", "Clique sur l'image : $imageUrl")
                Toast.makeText(
                    itemView.context, // Utilise le contexte de la vue
                    "Clique sur l'image : $imageUrl",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        /**
         * Méthode : cancelJob
         * Description : Annule le Job en cours pour ce ViewHolder.
         */
        fun cancelJob() {
            job?.cancel()
            job = null
        }
    }
}