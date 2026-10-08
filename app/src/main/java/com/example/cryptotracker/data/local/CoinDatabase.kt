package com.example.cryptotracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 2)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
}