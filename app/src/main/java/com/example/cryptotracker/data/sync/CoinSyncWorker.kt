package com.example.cryptotracker.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.repository.CoinRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// Фоновое обновление кэша монет: пользователь открывает приложение, а в Room уже свежие цены.
// Context и параметры даёт WorkManager (@Assisted), всё остальное подставляет Hilt.
@HiltWorker
class CoinSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: CoinRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            repository.refreshCoins()
            Result.success()
        } catch (e: DataException) {
            // Нет сети или сервер недоступен: пробуем позже с нарастающей паузой, а не ждём следующего часа
            Result.retry()
        }
    }
}
