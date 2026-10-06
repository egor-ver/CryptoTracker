package com.example.cryptotracker

import com.example.cryptotracker.data.local.CoinEntity
import com.example.cryptotracker.data.local.mapper.toCoin
import com.example.cryptotracker.data.local.mapper.toEntity
import com.example.cryptotracker.data.remote.dto.CoinDto
import com.example.cryptotracker.data.remote.mapper.toCoin
import com.example.cryptotracker.domain.model.Coin
import org.junit.Assert.assertEquals

import org.junit.Test


class CoinMapperTest {
    private val dto = CoinDto(
        current_price = 80.000,
        id = "btc",
        image = "imageLink",
        name = "Bitcoin",
        price_change_percentage_24h = 1.1,
        symbol = "BTC"
    )
    private val expectedCoin = Coin(
        price = 80.000,
        id = "btc",
        imageUrl = "imageLink",
        name = "Bitcoin",
        priceChange = 1.1,
        symbol = "BTC"
    )
    @Test
    fun `все поля переносятся из dto в domain `(){
        val coin  = dto.toCoin()
        assertEquals(expectedCoin, coin)
    }
    @Test
    fun `когда текущая цена null цена становится 0`(){
        val changedDto = dto.copy(current_price = null)
        assertEquals(0.0, changedDto.toCoin().price, 0.0001)
    }
    @Test
    fun `когда изменение цены за 24ч null маппер возвращает 0`(){
        val changedDto = dto.copy(price_change_percentage_24h = null)
        assertEquals(0.0, changedDto.toCoin().priceChange, 0.0001)
    }
    @Test
    fun `маппер переносит все поля при превращении в entity`(){
        val expectedEntity = CoinEntity(
            id = "btc", symbol = "BTC", name = "Bitcoin",
            imageUrl = "imageLink", price = 80.0, priceChange = 1.1
        )
        assertEquals(expectedEntity, expectedCoin.toEntity())
    }
    @Test
    fun `круговой проход 2 мапперами возвращает те же поля`(){
        assertEquals(expectedCoin, expectedCoin.toEntity().toCoin())
    }

}