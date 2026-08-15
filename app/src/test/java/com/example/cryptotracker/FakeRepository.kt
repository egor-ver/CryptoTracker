package com.example.cryptotracker

import com.example.cryptotracker.data.repository.CoinRepository
import com.example.cryptotracker.domain.model.Coin

class FakeRepository: CoinRepository{
    override suspend fun getCoins(): List<Coin> {
        return listOf(
            Coin(
                id = "id_1",
                price = 50000.0,
                imageUrl = "image_url",
                name = "Bitcoin",
                priceChange = 2.2,
                symbol = "BTC"
            ),
            Coin(
                id = "id_2",
                price = 3000.0,
                imageUrl = "image_url",
                name = "Ethereum",
                priceChange = 1.2,
                symbol = "ETH"
            )
        )
    }

}