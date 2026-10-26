package com.example.cryptotracker.testutil

import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.dto.CoinDto
import com.example.cryptotracker.data.remote.dto.MarketChartDto
class FakeCoinApi: CoinApi {
    var error: Exception? = null
    var chartError: Exception? = null
    var chartRequests = mutableListOf<Pair<String, Int>>()
    var chartPrices: List<List<Double>> = listOf(
        listOf(2_000.0, 20.0),
        listOf(1_000.0, 10.0),
        listOf(3_000.0)
    )
    override suspend fun getCoins(
        currency: String,
        order: String,
        perPage: Int,
        page: Int
    ): List<CoinDto> {
        error?.let { throw it }
        return listOf(CoinDto(
            id = "1",
            current_price = 70000.0,
            image = "FakeUrl",
            name = "BTC",
            symbol = "FakeSymbol",
            price_change_percentage_24h = 1.0
        ))
    }

    override suspend fun getMarketChart(id: String, currency: String, days: Int): MarketChartDto {
        chartRequests.add(id to days)
        chartError?.let { throw it }
        return MarketChartDto(chartPrices)
    }
}
