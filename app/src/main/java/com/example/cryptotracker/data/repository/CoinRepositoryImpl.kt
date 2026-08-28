package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.domain.model.Coin
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(private val api: CoinApi): CoinRepository{
    override suspend fun getCoins(): List<Coin> {
        return api.getCoins().map{it.toCoin()}
    }
}