package com.example.cryptotracker

import androidx.lifecycle.SavedStateHandle
import com.example.cryptotracker.ui.list.CoinDetailUiState
import com.example.cryptotracker.ui.list.CoinDetailViewModel
import com.example.cryptotracker.ui.list.LoadError
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

    private fun createViewModel(coinId: String?): CoinDetailViewModel {
        val args = if (coinId != null) mapOf("coinId" to coinId) else emptyMap()
        return CoinDetailViewModel(FakeRepository(), SavedStateHandle(args))
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
