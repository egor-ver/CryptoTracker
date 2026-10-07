package com.example.cryptotracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CoinDao{
    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<CoinEntity>
    @Query("SELECT * FROM coins WHERE id = :id")
    suspend fun getCoinById(id: String): CoinEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinEntity>)
    @Query("DELETE FROM coins")
    suspend fun clearAll()
    @Transaction
    suspend fun replaceAll(coins: List<CoinEntity>){
        clearAll()
        insertAll(coins)
    }
}