package com.example.android.marsrealestate

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.android.marsrealestate.work.MarsRealEstateWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarsRealEstateApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            WorkManager.getInstance(applicationContext)
                    .enqueueUniquePeriodicWork(
                            MarsRealEstateWork.WORK_NAME,
                            ExistingPeriodicWorkPolicy.KEEP,
                            MarsRealEstateWork.refreshPeriodicWorkRequest)
        }
    }
}