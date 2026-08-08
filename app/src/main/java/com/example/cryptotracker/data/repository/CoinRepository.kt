package com.example.cryptotracker.data.repository

import com.example.cryptotracker.domain.model.Coin

interface CoinRepository {
    suspend fun getCoins(): List<Coin>
}