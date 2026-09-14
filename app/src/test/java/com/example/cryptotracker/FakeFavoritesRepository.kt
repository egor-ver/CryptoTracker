package com.example.cryptotracker

import com.example.cryptotracker.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFavoritesRepository: FavoritesRepository{
    private val coins = MutableStateFlow<Set<String>>(emptySet())
    override fun observeFavorites(): Flow<Set<String>> {
        return coins
    }

    override suspend fun editFavorites(newFavorite: String) {
        coins.value = if(newFavorite in coins.value) coins.value - newFavorite else coins.value + newFavorite
    }

}