package com.example.cryptotracker.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 1)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
}