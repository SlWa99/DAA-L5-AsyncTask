/**
 * Nom du fichier : CacheScheduler.kt
 * Description    : Planification périodique pour le nettoyage du cache à l'aide de WorkManager.
 * Auteurs        : ICI
 * Date           : 1er décembre 2024
 */
package ch.heigvd.iict.daa.template

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Fonction : schedulePeriodicCacheCleaning
 * Description : Planifie une tâche périodique de nettoyage du cache en utilisant WorkManager.
 *               La tâche s'exécute toutes les 15 minutes en arrière-plan.
 * @param context Contexte de l'application ou de l'activité pour initialiser le WorkManager.
 */
fun schedulePeriodicCacheCleaning(context: Context) {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<CacheCleanerWorker>(
        15, TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "CacheCleanerWork",
        androidx.work.ExistingPeriodicWorkPolicy.KEEP,
        periodicWorkRequest
    )
    Log.d(context.getString(R.string.log_tag_worker), context.getString(R.string.log_periodic_schedule))
}