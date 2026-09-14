package com.example.cryptotracker.domain

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<Set<String>>
    suspend fun editFavorites(newFavorite: String)
}