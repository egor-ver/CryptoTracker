package com.example.cryptotracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.cryptotracker.data.sync.CoinSyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CryptoApp: Application(), Configuration.Provider {

    // Фабрика, которая умеет создавать воркеры с зависимостями из Hilt (WorkManager сам так не умеет)
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        CoinSyncScheduler.schedule(this)
    }
}
