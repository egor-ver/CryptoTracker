package com.example.cryptotracker.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val repository: CoinRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val coinId = savedStateHandle.get<String>("coinId")
    private val _uiState = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val uiState: StateFlow<CoinDetailUiState> = _uiState
    private fun loadCoin(){
        viewModelScope.launch {
            try{
                val selectedCoin = repository.getCoins().find { it.id == coinId }
                _uiState.value = if (selectedCoin != null) {
                    CoinDetailUiState.Success(selectedCoin)
                } else {
                    CoinDetailUiState.Error("Монета не найдена")
                }
            }
            catch (e: Exception){
                _uiState.value = CoinDetailUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
    init{
        loadCoin()
    }
}