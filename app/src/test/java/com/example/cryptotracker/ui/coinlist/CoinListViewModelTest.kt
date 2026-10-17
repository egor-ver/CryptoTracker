package com.example.cryptotracker.ui.coinlist

import com.example.cryptotracker.testutil.FakeFavoritesRepository
import com.example.cryptotracker.testutil.FakeRepository
import com.example.cryptotracker.testutil.MainDispatcherRule
import com.example.cryptotracker.ui.common.LoadError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

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
