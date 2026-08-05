package com.example.cryptotracker.data.remote.dto

data class CoinDto (
    // допиши: name, image, current_price, price_change_percentage_24h
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double,
    val price_change_percentage_24h: Double

)