package com.example.cryptotracker

import com.example.cryptotracker.data.remote.CoinApi
import com.example.cryptotracker.data.remote.dto.CoinDto
class FakeCoinApi: CoinApi {
    var error: Exception? = null
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
}