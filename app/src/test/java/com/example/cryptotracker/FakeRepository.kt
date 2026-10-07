package com.example.cryptotracker

import com.example.cryptotracker.domain.CoinRepository
import com.example.cryptotracker.domain.model.Coin

class FakeRepository(
    private val error: Exception? = null
): CoinRepository{
    var callCount = 0
        private set
    private val coins = listOf(Coin(id = "id_1",
        price = 50000.0,
        imageUrl = "image_url",
        name = "Bitcoin",
        priceChange = 2.2,
        symbol = "BTC"),
        Coin(id = "id_2",
            price = 3000.0,
            imageUrl = "image_url",
            name = "Ethereum",
            priceChange = 1.2,
            symbol = "ETH"))
    override suspend fun getCoins(): List<Coin> {
        callCount++
        error?.let{throw it}
        return coins
    }

    override suspend fun getCoin(id: String): Coin? {
        return coins.find { it.id == id }
    }
}