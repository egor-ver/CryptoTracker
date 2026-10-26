package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.local.db.CoinDao
import com.example.cryptotracker.data.local.mapper.toCoin
import com.example.cryptotracker.data.local.mapper.toEntity
import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.data.remote.mapper.toPricePoints
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.PricePoint
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
    private suspend fun fetchAndCache(): List<Coin> {
        val coins = api.getCoins().map { it.toCoin() }
        dao.replaceAll(coins.map{it.toEntity()})
        return coins
    }
    override suspend fun getCoins(): List<Coin> {
        return try{
            fetchAndCache()
        } catch (e: IOException){
            loadFromRoom(DataException.Network(e))
        } catch (e: HttpException){
            loadFromRoom(DataException.Server(e))
        }
    }

    // В отличие от getCoins не подменяет сетевую ошибку кэшем: фоновой синхронизации важно знать, что не вышло
    override suspend fun refreshCoins() {
        try {
            fetchAndCache()
        } catch (e: IOException) {
            throw DataException.Network(e)
        } catch (e: HttpException) {
            throw DataException.Server(e)
        }
    }

    override suspend fun getCoin(id: String): Coin? {
        return dao.getCoinById(id)?.toCoin()
    }

    // История цен нужна только пока открыт экран, поэтому в Room её не кладём: без сети графика просто не будет
    override suspend fun getPriceHistory(id: String, period: ChartPeriod): List<PricePoint> {
        return try {
            api.getMarketChart(id = id, days = period.days).toPricePoints()
        } catch (e: IOException) {
            throw DataException.Network(e)
        } catch (e: HttpException) {
            throw DataException.Server(e)
        }
    }
}