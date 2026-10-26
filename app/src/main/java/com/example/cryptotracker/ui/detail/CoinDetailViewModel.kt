package com.example.cryptotracker.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.domain.repository.CoinRepository
import com.example.cryptotracker.ui.common.LoadError
import com.example.cryptotracker.ui.common.toLoadError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private val _period = MutableStateFlow(ChartPeriod.WEEK)
    val period = _period.asStateFlow()
    private val _chartState = MutableStateFlow<PriceChartUiState>(PriceChartUiState.Loading)
    val chartState = _chartState.asStateFlow()

    // Уже загруженные периоды: переключение туда-обратно не бьёт по API (у бесплатного CoinGecko жёсткий лимит запросов)
    private val loadedPeriods = mutableMapOf<ChartPeriod, List<PricePoint>>()
    private var chartJob: Job? = null

    private fun loadCoin(){
        viewModelScope.launch {
            val selectedCoin = coinId?.let { repository.getCoin(it) }
            _uiState.value = if(selectedCoin != null) CoinDetailUiState.Success(selectedCoin)
            else CoinDetailUiState.Error(LoadError.NOT_FOUND)
        }
    }

    private fun loadChart(){
        val id = coinId ?: return
        val period = _period.value
        // Быстрый клик по периодам: прошлый запрос больше не нужен, иначе он может перезаписать свежий
        chartJob?.cancel()
        loadedPeriods[period]?.let {
            _chartState.value = PriceChartUiState.Success(it)
            return
        }
        _chartState.value = PriceChartUiState.Loading
        chartJob = viewModelScope.launch {
            try {
                val points = repository.getPriceHistory(id, period)
                if (points.size < 2) {
                    _chartState.value = PriceChartUiState.Error(LoadError.NO_DATA)
                } else {
                    loadedPeriods[period] = points
                    _chartState.value = PriceChartUiState.Success(points)
                }
            } catch (e: DataException) {
                _chartState.value = PriceChartUiState.Error(e.toLoadError())
            }
        }
    }

    fun onPeriodSelected(period: ChartPeriod){
        _period.value = period
        loadChart()
    }

    fun retryChart(){
        loadChart()
    }

    init{
        loadCoin()
        loadChart()
    }
}
