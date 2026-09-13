package com.example.cryptotracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.local.FavoritesDataStore
import com.example.cryptotracker.domain.CoinRepository
import com.example.cryptotracker.domain.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: CoinRepository,
    private val favoritesDataStore: FavoritesDataStore
): ViewModel(){

    private val _uiState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val uiState: StateFlow<CoinListUiState> = _uiState

    private val _searchState = MutableStateFlow("")
    val searchState: StateFlow<String> = _searchState
    val filteredCoins: StateFlow<List<Coin>> = _uiState.combine(_searchState){ ui, search ->
        if(ui is CoinListUiState.Success){
            ui.coins.filter {
                it.name.contains(search, ignoreCase = true) || it.symbol.contains(search, ignoreCase = true)
            }
        }
        else{
            emptyList()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    fun onQueryChanged(query: String){
        _searchState.value = query
    }

    private fun loadCoins(){
        _uiState.value = CoinListUiState.Loading
        viewModelScope.launch {
            try {
                val coins = repository.getCoins()
                _uiState.value = CoinListUiState.Success(coins)
            } catch (e: Exception) {
                _uiState.value = CoinListUiState.Error(e.message ?: "Ошибка сети")
            }
        }
    }
    val favorites: StateFlow<Set<String>> = favoritesDataStore.observeFavorites()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    fun editFavorite(id: String){
        viewModelScope.launch {
            favoritesDataStore.editFavorites(id)
        }
    }
    init{
        loadCoins()
    }
    fun retry() {
        loadCoins()
    }
}