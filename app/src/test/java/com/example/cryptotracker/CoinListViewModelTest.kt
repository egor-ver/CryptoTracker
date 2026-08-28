package com.example.cryptotracker

import com.example.cryptotracker.ui.list.CoinListUiState
import com.example.cryptotracker.ui.list.CoinListViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CoinListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `uiState Success test`() {
        val viewModel = CoinListViewModel(FakeRepository())

        val state = viewModel.uiState.value

        assertTrue(state is CoinListUiState.Success)
        assertEquals(2, (state as CoinListUiState.Success).coins.size)
    }
}
