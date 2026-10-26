package com.example.cryptotracker.data.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object CoinSyncScheduler {
    private const val WORK_NAME = "coin_sync"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<CoinSyncWorker>(1, TimeUnit.HOURS)
            // Без сети воркер не запускается совсем, батарея и трафик не тратятся
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()
        // KEEP: при каждом старте приложения не пересоздаём уже запланированную работу и не сбрасываем её таймер
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}
