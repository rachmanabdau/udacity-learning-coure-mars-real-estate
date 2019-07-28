package com.example.android.marsrealestate.work

import android.content.Context
import androidx.work.*
import com.example.android.marsrealestate.data.local.MarsDatabase
import com.example.android.marsrealestate.repository.Repository
import java.util.concurrent.TimeUnit

class MarsRealEstateWork(context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params) {

    private val database = MarsDatabase.getInstance(context)
    private val repository = Repository(database)

    companion object {
        const val WORK_NAME = "REFRESH_MARS_DATABASE"

        val refreshConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .apply {
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()

        val refreshPeriodicWorkRequest = PeriodicWorkRequestBuilder<MarsRealEstateWork>(
                1, TimeUnit.DAYS)
                .setConstraints(refreshConstraints)
                .build()
    }

    override suspend fun doWork(): Result {
        return try {
            repository.refreshData()
            Result.success()
        } catch (e: Throwable) {
            Result.retry()
        }
    }
}