package com.example.cryptotracker.data.remote.dto

// prices: список пар [время в мс, цена]. Gson читает числа в Double, время потом переводим в Long.
data class MarketChartDto(
    val prices: List<List<Double>>
)
