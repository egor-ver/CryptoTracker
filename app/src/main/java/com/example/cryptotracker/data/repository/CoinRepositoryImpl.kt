package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.local.db.CoinDao
import com.example.cryptotracker.data.local.mapper.toCoin
import com.example.cryptotracker.data.local.mapper.toEntity
import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.repository.CoinRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi,
    private val dao: CoinDao
): CoinRepository {
    private suspend fun loadFromRoom(e: DataException): List<Coin>{
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
            loadFromRoom(DataException.Network(e))
        } catch (e: HttpException){
            loadFromRoom(DataException.Server(e))
        }
    }

    override suspend fun getCoin(id: String): Coin? {
        return dao.getCoinById(id)?.toCoin()
    }
}