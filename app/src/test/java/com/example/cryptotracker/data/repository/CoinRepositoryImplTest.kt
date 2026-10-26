package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.local.db.CoinEntity
import com.example.cryptotracker.domain.error.DataException
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.testutil.FakeCoinApi
import com.example.cryptotracker.testutil.FakeCoinDao
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CoinRepositoryImplTest {
    private val api = FakeCoinApi()
    private val dao = FakeCoinDao()
    private val repository = CoinRepositoryImpl(api, dao)
    @Test
    fun `getCoins корректно возвращает и сохраняет монеты в DAO`() = runTest{
        val result = repository.getCoins()
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals(1, dao.stored.size)
        assertEquals("1", dao.stored[0].id)
    }
    @Test
    fun `когда нет сети, но кэш есть, отдаётся кэш`() = runTest{
        dao.stored.add(CoinEntity(
            id = "cached", symbol = "ETH", name = "Ethereum",
            imageUrl = "url", price = 3000.0, priceChange = 1.0
        ))
        api.error = IOException()
        val result = repository.getCoins()
        assertEquals(listOf("cached"), result.map { it.id })
    }
    @Test
    fun `когда нет сети и кэш пуст бросается DataException Network`() = runTest{
        api.error = IOException()
        try{
            repository.getCoins()
            fail("Исключение не вылетело")
        } catch (e: DataException.Network) {
        }
    }
    @Test
    fun `ошибка сервера при пустом кэше бросается как DataException Server`() = runTest{
        api.error = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        try{
            repository.getCoins()
            fail("Исключение не вылетело")
        } catch (e: DataException.Server) {
        }
    }
    @Test
    fun `история цен просит у API нужное число дней и сортирует точки`() = runTest{
        val points = repository.getPriceHistory("btc", ChartPeriod.MONTH)

        assertEquals(listOf("btc" to 30), api.chartRequests)
        assertEquals(listOf(1_000L, 2_000L), points.map { it.timestamp })
    }
    @Test
    fun `история цен без сети бросает DataException Network`() = runTest{
        api.chartError = IOException()
        try{
            repository.getPriceHistory("btc", ChartPeriod.DAY)
            fail("Исключение не вылетело")
        } catch (e: DataException.Network) {
        }
    }
    @Test
    fun `история цен при лимите запросов 429 бросает DataException Server`() = runTest{
        api.chartError = HttpException(Response.error<Any>(429, "".toResponseBody(null)))
        try{
            repository.getPriceHistory("btc", ChartPeriod.DAY)
            fail("Исключение не вылетело")
        } catch (e: DataException.Server) {
        }
    }
    @Test
    fun `ошибка сервера при непустом кэше отдаёт кэш`() = runTest{
        dao.stored.add(CoinEntity(
            id = "cached", symbol = "ETH", name = "Ethereum",
            imageUrl = "url", price = 3000.0, priceChange = 1.0
        ))
        api.error = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        assertEquals(listOf("cached"), repository.getCoins().map { it.id })
    }
}