package com.example.cryptotracker.ui.list

import com.example.cryptotracker.domain.model.Coin

sealed interface CoinListUiState{
    object Loading: CoinListUiState
    data class Success(val coins: List<Coin>): CoinListUiState
    data class Error(val message: String): CoinListUiState
}