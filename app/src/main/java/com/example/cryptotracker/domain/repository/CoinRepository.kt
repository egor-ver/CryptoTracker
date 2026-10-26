package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.PricePoint

interface CoinRepository{
    suspend fun getCoins(): List<Coin>
    suspend fun refreshCoins()
    suspend fun getCoin(id: String): Coin?
    suspend fun getPriceHistory(id: String, period: ChartPeriod): List<PricePoint>
}