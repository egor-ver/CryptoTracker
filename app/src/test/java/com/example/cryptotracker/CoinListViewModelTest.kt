package com.example.cryptotracker

import com.example.cryptotracker.ui.list.CoinListUiState
import com.example.cryptotracker.ui.list.CoinListViewModel
import com.example.cryptotracker.ui.list.LoadError
import java.io.IOException

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `uiState становится Success `() = runTest(mainDispatcherRule.testDispatcher){
        val viewModel = CoinListViewModel(FakeRepository(), FakeFavoritesRepository())
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is CoinListUiState.Success)
        assertEquals(2, (state as CoinListUiState.Success).coins.size)
    }
    @Test
    fun `uiState сообщение ошибки нет сети при исключении без сообщения`() = runTest(mainDispatcherRule.testDispatcher){
        val fakeRepository = FakeRepository(IOException())
        val viewModel = CoinListViewModel(fakeRepository, FakeFavoritesRepository())
        advanceUntilIdle()
        val uiState = viewModel.uiState
        assertEquals(CoinListUiState.Error(LoadError.NETWORK), uiState.value)
    }
    @Test
    fun `после retry репозиторий вызван дважды`() = runTest(mainDispatcherRule.testDispatcher){
        val fakeRepository = FakeRepository()
        val viewModel = CoinListViewModel(fakeRepository, FakeFavoritesRepository())
        advanceUntilIdle()
        viewModel.retry()
        advanceUntilIdle()
        val callCount = fakeRepository.callCount
        assertEquals(2, callCount)
    }
}
