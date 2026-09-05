package com.example.cryptotracker.ui.list

import com.example.cryptotracker.domain.model.Coin

sealed interface CoinDetailUiState {
    data class Success(val coin: Coin): CoinDetailUiState
    data class Error(val message: String) : CoinDetailUiState
    object Loading: CoinDetailUiState
}