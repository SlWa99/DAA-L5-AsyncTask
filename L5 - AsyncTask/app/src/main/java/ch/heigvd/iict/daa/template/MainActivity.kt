/**
 * Nom du fichier : MainActivity.kt
 * Description    : Activité principale de l'application qui affiche une grille d'images numérotées
 *                  en utilisant une RecyclerView avec un GridLayoutManager.
 * Auteurs        : ICI
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Classe : MainActivity
 * Description : Activité principale de l'application qui gère l'affichage de la grille d'images,
 *               le menu des options, et les interactions liées au cache des images.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Méthode : onCreate
     * Description : Initialise l'activité, configure la RecyclerView et son adaptateur.
     * @param savedInstanceState Instance sauvegardée de l'activité.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = Adapter(this)

        //ImageCache.initialize(this)
        schedulePeriodicCacheCleaning(this)
    }

    /**
     * Méthode : onCreateOptionsMenu
     * Description : Gonfle le menu des options pour l'activité.
     * @param menu Objet Menu où les options sont ajoutées.
     * @return true si le menu a été créé avec succès.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Méthode : onOptionsItemSelected
     * Description : Gère les interactions avec les éléments du menu des options.
     * @param item Élément sélectionné dans le menu.
     * @return true si l'action a été gérée, sinon appelle le parent.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_reload_cache) {
            reloadData()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Méthode : reloadData
     * Description : Vide le cache des images et affiche un message à l'utilisateur.
     */
    private fun reloadData() {
        ImageCache.clear()
        Toast.makeText(this, getString(R.string.toast_cache_cleared), Toast.LENGTH_SHORT).show()
    }
}