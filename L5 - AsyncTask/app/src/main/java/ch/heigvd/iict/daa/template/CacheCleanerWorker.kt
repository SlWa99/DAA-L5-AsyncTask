package ch.heigvd.iict.daa.template

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CacheCleanerWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val cacheDir = applicationContext.cacheDir
        cacheDir.listFiles()?.forEach { it.delete() }
        Log.d(applicationContext.getString(R.string.log_tag_worker), applicationContext.getString(R.string.log_cache_cleaned))
        return Result.success()
    }
}