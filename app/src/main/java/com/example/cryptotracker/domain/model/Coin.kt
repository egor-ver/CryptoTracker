package com.example.cryptotracker.domain.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val price: Double,
    val priceChangePercentage: Double
)