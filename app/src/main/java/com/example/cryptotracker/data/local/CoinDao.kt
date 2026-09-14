package com.example.cryptotracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao{
    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<CoinEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinEntity>)
    @Query("DELETE FROM coins")
    suspend fun clearAll()
}