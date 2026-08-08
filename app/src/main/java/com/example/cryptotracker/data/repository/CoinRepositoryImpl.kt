package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.remote.RetrofitClient
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.domain.model.Coin

class CoinRepositoryImpl: CoinRepository {
    override suspend fun getCoins(): List<Coin> {
        return RetrofitClient.api.getCoins().map{it.toCoin()}

    }
}