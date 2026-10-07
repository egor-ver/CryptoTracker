package com.example.cryptotracker.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val repository: CoinRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val coinId = savedStateHandle.get<String>("coinId")
    private val _uiState = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private fun loadCoin(){
        viewModelScope.launch {
            val selectedCoin = coinId?.let { repository.getCoin(it) }
            _uiState.value = if(selectedCoin != null) CoinDetailUiState.Success(selectedCoin)
            else CoinDetailUiState.Error("Монета не найдена")
        }
    }
    init{
        loadCoin()
    }
}