package com.example.cryptotracker.di

import android.content.Context
import androidx.room.Room
import com.example.cryptotracker.data.local.db.CoinDao
import com.example.cryptotracker.data.local.db.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CoinDatabase{
        // Это только кэш с сервера, поэтому при смене схемы базу проще пересоздать, чем писать миграции
        return Room.databaseBuilder(context, CoinDatabase::class.java, "coins.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(database: CoinDatabase): CoinDao{
        return database.coinDao()
    }
}
