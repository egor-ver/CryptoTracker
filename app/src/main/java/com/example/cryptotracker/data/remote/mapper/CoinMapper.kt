package com.example.cryptotracker.data.remote.mapper

import com.example.cryptotracker.data.remote.dto.CoinDto
import com.example.cryptotracker.domain.model.Coin

fun CoinDto.toCoin(): Coin{
    return Coin(
        id = this.id,
        imageUrl = this.image,
        name = this.name,
        price = this.current_price,
        priceChangePercentage = this.price_change_percentage_24h,
        symbol = this.symbol
    )
}