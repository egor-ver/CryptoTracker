package com.example.cryptotracker.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.testutil.FakeRepository
import com.example.cryptotracker.testutil.MainDispatcherRule
import com.example.cryptotracker.ui.common.LoadError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        coinId: String?,
        repository: FakeRepository = FakeRepository()
    ): CoinDetailViewModel {
        val args = if (coinId != null) mapOf("coinId" to coinId) else emptyMap()
        return CoinDetailViewModel(repository, SavedStateHandle(args))
    }

    @Test
    fun `график по умолчанию за неделю и загружается при открытии`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository()
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()

        assertEquals(ChartPeriod.WEEK, viewModel.period.value)
        assertEquals(listOf(ChartPeriod.WEEK), repository.historyRequests)
        assertEquals(PriceChartUiState.Success(repository.historyPoints), viewModel.chartState.value)
    }

    @Test
    fun `смена периода запрашивает новые данные`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository()
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()

        viewModel.onPeriodSelected(ChartPeriod.YEAR)
        advanceUntilIdle()

        assertEquals(ChartPeriod.YEAR, viewModel.period.value)
        assertEquals(listOf(ChartPeriod.WEEK, ChartPeriod.YEAR), repository.historyRequests)
    }

    @Test
    fun `возврат на уже загруженный период не ходит в сеть`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository()
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()
        viewModel.onPeriodSelected(ChartPeriod.DAY)
        advanceUntilIdle()

        viewModel.onPeriodSelected(ChartPeriod.WEEK)
        advanceUntilIdle()

        assertEquals(listOf(ChartPeriod.WEEK, ChartPeriod.DAY), repository.historyRequests)
        assertTrue(viewModel.chartState.value is PriceChartUiState.Success)
    }

    @Test
    fun `ошибка сети на графике не ломает карточку монеты`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository().apply { historyError = DataException.Network() }
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()

        assertEquals(PriceChartUiState.Error(LoadError.NETWORK), viewModel.chartState.value)
        assertTrue(viewModel.uiState.value is CoinDetailUiState.Success)
    }

    @Test
    fun `повторить после ошибки графика загружает его заново`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository().apply { historyError = DataException.Server() }
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()
        assertEquals(PriceChartUiState.Error(LoadError.SERVER), viewModel.chartState.value)

        repository.historyError = null
        viewModel.retryChart()
        advanceUntilIdle()

        assertTrue(viewModel.chartState.value is PriceChartUiState.Success)
    }

    @Test
    fun `меньше двух точек это нет данных, а не пустой график`() = runTest(mainDispatcherRule.testDispatcher){
        val repository = FakeRepository().apply { historyPoints = listOf(PricePoint(1_000, 10.0)) }
        val viewModel = createViewModel("id_1", repository)
        advanceUntilIdle()

        assertEquals(PriceChartUiState.Error(LoadError.NO_DATA), viewModel.chartState.value)
    }

    @Test
    fun `монета из репозитория попадает в Success`() = runTest(mainDispatcherRule.testDispatcher){
        val viewModel = createViewModel("id_1")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is CoinDetailUiState.Success)
        assertEquals("Bitcoin", (state as CoinDetailUiState.Success).coin.name)
    }

    @Test
    fun `неизвестный id даёт ошибку монета не найдена`() = runTest(mainDispatcherRule.testDispatcher){
        val viewModel = createViewModel("unknown")
        advanceUntilIdle()

        assertEquals(CoinDetailUiState.Error(LoadError.NOT_FOUND), viewModel.uiState.value)
    }

    @Test
    fun `без id ошибка, а не вечная загрузка`() = runTest(mainDispatcherRule.testDispatcher){
        val viewModel = createViewModel(null)
        advanceUntilIdle()

        assertEquals(CoinDetailUiState.Error(LoadError.NOT_FOUND), viewModel.uiState.value)
    }
}
