package com.example.cryptotracker.domain

import com.example.cryptotracker.domain.model.Coin

interface CoinRepository{
    suspend fun getCoins(): List<Coin>
    suspend fun getCoin(id: String): Coin?
}