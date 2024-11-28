package ch.heigvd.iict.daa.template

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.net.URL

class Adapter(private val lifecycleScope: LifecycleOwner) : RecyclerView.Adapter<Adapter.ImageViewHolder>() {

    // Liste des URLs des images
    private val imageUrls = (1..100).map { "https://daa.iict.ch/images/$it.jpg" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        // Lancer une coroutine pour télécharger et afficher l'image
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(imageUrl: String) {
            // Lancement d'une coroutine
            val url = URL(imageUrl)
            lifecycleScope.lifecycleScope.launch {
                val bytes = downloadImage(url)
                val bmp = decodeImage(bytes)
                displayImage(imageView, bmp)
            }
        }
    }
} 