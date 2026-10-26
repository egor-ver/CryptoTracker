package com.example.cryptotracker.data.remote.mapper

import com.example.cryptotracker.data.remote.dto.MarketChartDto
import com.example.cryptotracker.domain.model.PricePoint

// Битые пары (меньше двух чисел) пропускаем, а не падаем: график лучше нарисовать без одной точки.
fun MarketChartDto.toPricePoints(): List<PricePoint> {
    return prices
        .filter { it.size >= 2 }
        .map { PricePoint(timestamp = it[0].toLong(), price = it[1]) }
        .sortedBy { it.timestamp }
}
