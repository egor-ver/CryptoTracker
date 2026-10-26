package com.example.cryptotracker.ui.common

import com.example.cryptotracker.domain.model.ChartPeriod
import java.math.MathContext
import java.text.SimpleDateFormat
import java.util.Date
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

// На какой процент цена изменилась от from до to. Нулевая стартовая цена не должна давать Infinity.
fun percentChange(from: Double, to: Double): Double {
    if (from == 0.0) return 0.0
    return (to - from) / from * 100
}

// Подпись точки на графике: на сутки важно время, на длинных периодах уже дата
fun formatChartDate(timestamp: Long, period: ChartPeriod): String {
    val pattern = when (period) {
        ChartPeriod.DAY -> "HH:mm"
        ChartPeriod.WEEK -> "d MMM, HH:mm"
        ChartPeriod.MONTH -> "d MMM"
        ChartPeriod.YEAR -> "d MMM yyyy"
    }
    return SimpleDateFormat(pattern, Locale("ru")).format(Date(timestamp))
}

fun ChartPeriod.label(): String = when (this) {
    ChartPeriod.DAY -> "24ч"
    ChartPeriod.WEEK -> "7д"
    ChartPeriod.MONTH -> "30д"
    ChartPeriod.YEAR -> "1г"
}
