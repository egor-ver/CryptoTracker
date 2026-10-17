package com.example.cryptotracker.di

import com.example.cryptotracker.data.local.datastore.FavoritesDataStore
import com.example.cryptotracker.data.repository.CoinRepositoryImpl
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Binds
    abstract fun bindRepository(impl: CoinRepositoryImpl): CoinRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesDatastore(impl: FavoritesDataStore): FavoritesRepository
}
