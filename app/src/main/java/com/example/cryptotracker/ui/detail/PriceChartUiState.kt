package com.example.cryptotracker.ui.detail

import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.ui.common.LoadError

// Отдельно от CoinDetailUiState: если график не загрузился, карточка монеты всё равно остаётся на экране.
sealed interface PriceChartUiState {
    data class Success(val points: List<PricePoint>) : PriceChartUiState
    data class Error(val error: LoadError) : PriceChartUiState
    object Loading : PriceChartUiState
}
