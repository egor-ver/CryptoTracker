package com.example.cryptotracker

import com.example.cryptotracker.ui.list.CoinListUiState
import com.example.cryptotracker.ui.list.CoinListViewModel
import com.example.cryptotracker.ui.list.LoadError
import java.io.IOException

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
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
    fun `uiState ошибка сервера при HttpException`() = runTest(mainDispatcherRule.testDispatcher){
        val error = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        val viewModel = CoinListViewModel(FakeRepository(error), FakeFavoritesRepository())
        advanceUntilIdle()
        assertEquals(CoinListUiState.Error(LoadError.SERVER), viewModel.uiState.value)
    }
    @Test
    fun `в избранном только отмеченные монеты`() = runTest(mainDispatcherRule.testDispatcher){
        val viewModel = CoinListViewModel(FakeRepository(), FakeFavoritesRepository())
        // favoriteCoins считается только пока на него кто-то подписан (WhileSubscribed)
        backgroundScope.launch { viewModel.favoriteCoins.collect {} }
        advanceUntilIdle()

        viewModel.editFavorite("id_2")
        advanceUntilIdle()

        assertEquals(listOf("id_2"), viewModel.favoriteCoins.value.map { it.id })
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
