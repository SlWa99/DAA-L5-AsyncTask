/**
 * Nom du fichier : CacheCleanerWorker.kt
 * Description    : Implémentation d'une tâche WorkManager pour le nettoyage périodique du cache.
 * Auteurs        : ICI
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Classe : CacheCleanerWorker
 * Description : Classe qui définit une tâche WorkManager pour supprimer tous les fichiers du répertoire
 *               de cache de l'application. Cette tâche est exécutée en arrière-plan.
 * @param context Contexte de l'application.
 * @param workerParams Paramètres fournis par WorkManager.
 */
class CacheCleanerWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    /**
     * Méthode : doWork
     * Description : Exécute la tâche de nettoyage du cache. Supprime tous les fichiers présents
     *               dans le répertoire de cache de l'application.
     * @return Résultat de l'exécution (succès ou échec).
     */
    override fun doWork(): Result {
        val cacheDir = applicationContext.cacheDir
        cacheDir.listFiles()?.forEach { it.delete() }
        Log.d(applicationContext.getString(R.string.log_tag_worker), applicationContext.getString(R.string.log_cache_cleaned))
        return Result.success()
    }
}