package com.example.cryptotracker

import com.example.cryptotracker.data.remote.dto.CoinDto
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.data.repository.CoinRepository
import com.example.cryptotracker.domain.model.Coin
import org.junit.Test
import kotlin.test.assertEquals

class CoinTest {
    @Test
    fun `test CoinMapper`(){
        val dto = CoinDto(
            id = "id_1",
            current_price = 50000.0,
            image = "image_url",
            name = "Bitcoin",
            price_change_percentage_24h = 2.2,
            symbol = "BTC"
        )
        val coin = dto.toCoin()
        assertEquals("id_1", coin.id)
        assertEquals(50000.0, coin.price, 0.0)
        assertEquals("image_url", coin.imageUrl)
        assertEquals("Bitcoin", coin.name)
        assertEquals(2.2, coin.priceChange, 0.0)
        assertEquals("BTC", coin.symbol)
    }
}
