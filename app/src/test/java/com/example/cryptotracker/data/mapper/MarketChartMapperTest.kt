package com.example.cryptotracker.data.mapper

import com.example.cryptotracker.data.remote.dto.MarketChartDto
import com.example.cryptotracker.data.remote.mapper.toPricePoints
import com.example.cryptotracker.domain.model.PricePoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketChartMapperTest {
    @Test
    fun `пары время и цена превращаются в точки`() {
        val dto = MarketChartDto(listOf(listOf(1_700_000_000_000.0, 65000.5)))

        assertEquals(listOf(PricePoint(1_700_000_000_000, 65000.5)), dto.toPricePoints())
    }

    @Test
    fun `точки идут по времени, даже если пришли вперемешку`() {
        val dto = MarketChartDto(listOf(listOf(3.0, 30.0), listOf(1.0, 10.0), listOf(2.0, 20.0)))

        assertEquals(listOf(1L, 2L, 3L), dto.toPricePoints().map { it.timestamp })
    }

    @Test
    fun `битая пара без цены пропускается`() {
        val dto = MarketChartDto(listOf(listOf(1.0, 10.0), listOf(2.0)))

        assertEquals(1, dto.toPricePoints().size)
    }

    @Test
    fun `пустой ответ даёт пустой список`() {
        assertTrue(MarketChartDto(emptyList()).toPricePoints().isEmpty())
    }
}
