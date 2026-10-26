package com.example.cryptotracker.ui.common

import org.junit.Test
import kotlin.test.assertEquals

class FormattersTest {
    @Test
    fun `крупная цена с разделителем тысяч и точкой`() {
        assertEquals("$65,000.00", formatPrice(65000.0))
        assertEquals("$1.50", formatPrice(1.5))
    }

    @Test
    fun `цена меньше доллара не превращается в ноль`() {
        assertEquals("$0.1235", formatPrice(0.1234567))
        assertEquals("$0.00001234", formatPrice(0.00001234))
        assertEquals("$0.50", formatPrice(0.5))
        assertEquals("$0.00", formatPrice(0.0))
    }

    @Test
    fun `неизвестная цена показывается прочерком`() {
        assertEquals("—", formatPrice(null))
    }

    @Test
    fun `изменение со знаком`() {
        assertEquals("+2.50%", formatChange(2.5))
        assertEquals("-1.20%", formatChange(-1.2))
    }

    @Test
    fun `процент изменения между двумя ценами`() {
        assertEquals(25.0, percentChange(100.0, 125.0), 0.0001)
        assertEquals(-50.0, percentChange(200.0, 100.0), 0.0001)
    }

    @Test
    fun `нулевая стартовая цена не даёт бесконечность`() {
        assertEquals(0.0, percentChange(0.0, 100.0), 0.0)
    }
}
