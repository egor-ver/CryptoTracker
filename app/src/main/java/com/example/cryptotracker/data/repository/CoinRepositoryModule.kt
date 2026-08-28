package com.example.cryptotracker.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoinRepositoryModule{
    @Binds
    abstract fun bindRepository(impl: CoinRepositoryImpl): CoinRepository
}