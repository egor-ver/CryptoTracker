package com.example.cryptotracker.ui.detail

import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.common.LoadError

sealed interface CoinDetailUiState {
    data class Success(val coin: Coin): CoinDetailUiState
    data class Error(val error: LoadError) : CoinDetailUiState
    object Loading: CoinDetailUiState
}