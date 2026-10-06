package com.example.cryptotracker

import com.example.cryptotracker.data.repository.CoinRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
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
    fun `когда нет сети и кэш пуст бросается IOException`() = runTest{
        api.error = IOException()
        try{
            repository.getCoins()
            fail("Исключение не вылетело")
        } catch (e: IOException) {
        }
    }
}