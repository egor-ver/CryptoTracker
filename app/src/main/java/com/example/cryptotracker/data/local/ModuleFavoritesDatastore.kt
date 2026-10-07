package com.example.cryptotracker.data.local

import com.example.cryptotracker.domain.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModuleFavoritesDatastore{
    @Binds
    @Singleton
    abstract fun bindFavoritesDatastore(impl: FavoritesDataStore): FavoritesRepository
}