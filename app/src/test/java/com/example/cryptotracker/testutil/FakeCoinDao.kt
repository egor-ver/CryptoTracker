package com.example.cryptotracker.testutil

import com.example.cryptotracker.data.local.db.CoinDao
import com.example.cryptotracker.data.local.db.CoinEntity

class FakeCoinDao: CoinDao {
    val stored = mutableListOf<CoinEntity>()
    override suspend fun getCoins(): List<CoinEntity> {
        return stored.toList()
    }

    override suspend fun clearAll() {
        stored.clear()
    }

    override suspend fun insertAll(coins: List<CoinEntity>) {
        stored.addAll(coins)
    }

    override suspend fun replaceAll(coins: List<CoinEntity>) {
        stored.clear()
        stored.addAll(coins)
    }

    override suspend fun getCoinById(id: String): CoinEntity? {
        return stored.find { it.id == id }
    }
}