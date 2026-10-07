package com.example.cryptotracker.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptotracker.domain.FavoritesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "favorites")
private val FAVORITES_KEY = stringSetPreferencesKey("favorite_ids")
class FavoritesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
): FavoritesRepository{
    override fun observeFavorites(): Flow<Set<String>>{
        return context.dataStore.data.map { prefs -> prefs[FAVORITES_KEY] ?: emptySet() }
    }
    override suspend fun editFavorites(newFavorite: String){
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            if(newFavorite in current) prefs[FAVORITES_KEY] = current - newFavorite
            else prefs[FAVORITES_KEY] = current + newFavorite
        }
    }
}