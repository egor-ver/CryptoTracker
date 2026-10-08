package com.example.cryptotracker.ui.format

import java.math.MathContext
import java.util.Locale

// Цены в долларах, поэтому формат всегда US: точка и запятая-разделитель тысяч
// не зависят от языка телефона. Если API не прислал цену, показываем прочерк, а не $0.00.
fun formatPrice(price: Double?): String {
    if (price == null) return "—"
    val number = if (price >= 1) {
        String.format(Locale.US, "%,.2f", price)
    } else {
        // Мелкие монеты (SHIB, PEPE ~0.00001): 4 значащие цифры, но не меньше 2 знаков после точки
        val rounded = price.toBigDecimal().round(MathContext(4)).stripTrailingZeros()
        val scaled = if (rounded.scale() < 2) rounded.setScale(2) else rounded
        scaled.toPlainString()
    }
    return "$$number"
}

fun formatChange(change: Double): String {
    val sign = if (change >= 0) "+" else ""
    return sign + String.format(Locale.US, "%.2f", change) + "%"
}
