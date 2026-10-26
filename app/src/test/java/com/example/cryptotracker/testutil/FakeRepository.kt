package com.example.cryptotracker.testutil

import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.domain.repository.CoinRepository

class FakeRepository(
    private val error: Exception? = null
): CoinRepository{
    var callCount = 0
        private set
    // Ошибка и данные истории цен меняются прямо в тесте: можно сломать запрос, а потом «починить» и нажать «Повторить»
    var historyError: Exception? = null
    var historyPoints: List<PricePoint> = listOf(
        PricePoint(1_000, 10.0),
        PricePoint(2_000, 15.0),
        PricePoint(3_000, 12.0)
    )
    val historyRequests = mutableListOf<ChartPeriod>()
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

    var refreshCount = 0
        private set
    override suspend fun refreshCoins() {
        refreshCount++
        error?.let { throw it }
    }

    override suspend fun getCoin(id: String): Coin? {
        return coins.find { it.id == id }
    }

    override suspend fun getPriceHistory(id: String, period: ChartPeriod): List<PricePoint> {
        historyRequests.add(period)
        historyError?.let { throw it }
        return historyPoints
    }
}
