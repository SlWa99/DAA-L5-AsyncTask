package ch.heigvd.iict.daa.template

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

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