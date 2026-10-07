package com.example.cryptotracker.data.repository


import com.example.cryptotracker.data.local.CoinDao
import com.example.cryptotracker.data.local.mapper.toCoin
import com.example.cryptotracker.data.local.mapper.toEntity
import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.domain.CoinRepository
import com.example.cryptotracker.domain.model.Coin
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi,
    private val dao: CoinDao
): CoinRepository {
    private suspend fun loadFromRoom(e: Exception): List<Coin>{
        val coins = dao.getCoins().map { it.toCoin() }
        if(coins.isEmpty()) throw e
        return coins

    }
    override suspend fun getCoins(): List<Coin> {
        return try{
            val coins = api.getCoins().map { it.toCoin() }
            dao.replaceAll(coins.map{it.toEntity()})
            coins
        } catch (e: IOException){
            loadFromRoom(e)
        } catch (e: HttpException){
            loadFromRoom(e)
        }
    }

    override suspend fun getCoin(id: String): Coin? {
        return dao.getCoinById(id)?.toCoin()
    }
}