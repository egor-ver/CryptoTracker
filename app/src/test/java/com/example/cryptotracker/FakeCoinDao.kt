package com.example.cryptotracker

import com.example.cryptotracker.data.local.CoinDao
import com.example.cryptotracker.data.local.CoinEntity

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
}