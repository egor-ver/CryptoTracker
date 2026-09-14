package com.example.cryptotracker.data.local.mapper

import com.example.cryptotracker.data.local.CoinEntity
import com.example.cryptotracker.domain.model.Coin

fun Coin.toEntity(): CoinEntity{
    return CoinEntity(
        id = this.id,
        imageUrl = this.imageUrl,
        name = this.name,
        price = this.price,
        priceChange = this.priceChange,
        symbol = this.symbol
    )
}

fun CoinEntity.toCoin(): Coin{
    return Coin(
        id = this.id,
        imageUrl = this.imageUrl,
        name = this.name,
        price = this.price,
        priceChange = this.priceChange,
        symbol = this.symbol
    )
}