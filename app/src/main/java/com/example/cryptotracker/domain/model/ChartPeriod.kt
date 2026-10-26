package com.example.cryptotracker.domain.model

// days уходит в параметр days запроса market_chart: CoinGecko сам выбирает частоту точек
// (для 1 дня это минуты, для 7-90 дней часы, дальше сутки).
enum class ChartPeriod(val days: Int) {
    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365)
}
