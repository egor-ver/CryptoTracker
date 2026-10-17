package com.example.cryptotracker.ui.coinlist

import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.common.LoadError

sealed interface CoinListUiState{
    object Loading: CoinListUiState
    data class Success(val coins: List<Coin>): CoinListUiState
    data class Error(val error: LoadError): CoinListUiState
}