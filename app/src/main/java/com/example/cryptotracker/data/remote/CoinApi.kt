package com.example.cryptotracker.data.remote

import com.example.cryptotracker.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi{
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1
    ): List<CoinDto>

}