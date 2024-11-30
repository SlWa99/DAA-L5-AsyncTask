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

class Adapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<Adapter.ImageViewHolder>() {

    // Liste des URLs des images
    private val imageUrls = (1..10000).map { "https://daa.iict.ch/images/$it.jpg" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: View = itemView.findViewById(R.id.progressBar)

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