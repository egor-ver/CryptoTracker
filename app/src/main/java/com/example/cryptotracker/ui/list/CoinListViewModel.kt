package com.example.cryptotracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CoinRepository
import com.example.cryptotracker.data.repository.CoinRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoinListViewModel: ViewModel(){
    private val repository: CoinRepository = CoinRepositoryImpl()
    private val _uiState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val uiState: StateFlow<CoinListUiState> = _uiState
    init{
        loadCoins()
    }
    private fun loadCoins(){
        viewModelScope.launch {
            _uiState.value = CoinListUiState.Loading
            try{
                val coins = repository.getCoins()
                _uiState.value = CoinListUiState.Success(coins)
            }
            catch (e: Exception){
                _uiState.value = CoinListUiState.Error(e.message ?: "Ошибка сети")
            }
        }
    }
}