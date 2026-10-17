package com.example.cryptotracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<Set<String>>
    suspend fun editFavorites(newFavorite: String)
}