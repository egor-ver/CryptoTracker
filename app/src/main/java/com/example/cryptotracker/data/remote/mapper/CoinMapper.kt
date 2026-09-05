package com.example.cryptotracker.data.remote.mapper

import com.example.cryptotracker.data.remote.dto.CoinDto
import com.example.cryptotracker.domain.model.Coin

fun CoinDto.toCoin(): Coin{
    return Coin(
        id = id,
        name = name,
        imageUrl = image,
        price = current_price ?: 0.0,
        priceChange = price_change_percentage_24h ?: 0.0,
        symbol = symbol
    )
}