package ch.heigvd.iict.daa.template

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Configuration de la RecyclerView avec un GridLayoutManager pour 3 colonnes
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Configuration de l'adaptateur
        val adapter = Adapter(this)
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_reload_cache) {
            // Action à effectuer pour recharger
            Toast.makeText(this, "Reload action triggered", Toast.LENGTH_SHORT).show()
            // Appelle ta méthode de rechargement ici
            reloadData()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // TODO : INVESITGUER
    private fun reloadData() {
        // Ajoute ici le code pour recharger les données
        // Par exemple : recharger une RecyclerView ou une autre vue
    }


}