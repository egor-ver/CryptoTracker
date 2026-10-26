package com.example.cryptotracker.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.ui.common.formatPrice
import com.example.cryptotracker.ui.common.label
import com.example.cryptotracker.ui.common.message
import com.example.cryptotracker.ui.components.ChangePill
import com.example.cryptotracker.ui.components.ErrorWithRetry
import com.example.cryptotracker.ui.components.LoadingBox
import com.example.cryptotracker.ui.components.PriceChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    state: CoinDetailUiState,
    chartState: PriceChartUiState,
    period: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit,
    onChartRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when (state) {
            is CoinDetailUiState.Loading -> Box(modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is CoinDetailUiState.Error -> Box(modifier, contentAlignment = Alignment.Center) {
                Text(state.error.message())
            }
            is CoinDetailUiState.Success -> CoinDetailContent(
                coin = state.coin,
                chartState = chartState,
                period = period,
                onPeriodSelected = onPeriodSelected,
                onChartRetry = onChartRetry,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CoinDetailContent(
    coin: Coin,
    chartState: PriceChartUiState,
    period: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit,
    onChartRetry: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = coin.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = coin.symbol.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = formatPrice(coin.price),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))
        PriceChartSection(chartState, period, onPeriodSelected, onChartRetry)
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Изменение за 24ч", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ChangePill(coin.priceChange)
                }
            }
        }
    }
}

// Высота загрузки и ошибки равна высоте графика с заголовком, чтобы карточка не прыгала при смене состояния
private val ChartAreaHeight = 290.dp

@Composable
private fun PriceChartSection(
    chartState: PriceChartUiState,
    period: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            PeriodSelector(period, onPeriodSelected)
            Spacer(Modifier.height(20.dp))
            val areaModifier = Modifier
                .fillMaxWidth()
                .height(ChartAreaHeight)
            when (chartState) {
                is PriceChartUiState.Loading -> LoadingBox(areaModifier)
                is PriceChartUiState.Error -> ErrorWithRetry(chartState.error, onRetry, areaModifier)
                is PriceChartUiState.Success -> PriceChart(chartState.points, period)
            }
        }
    }
}

@Composable
private fun PeriodSelector(period: ChartPeriod, onPeriodSelected: (ChartPeriod) -> Unit) {
    val periods = ChartPeriod.entries
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        periods.forEachIndexed { index, item ->
            SegmentedButton(
                selected = item == period,
                onClick = { onPeriodSelected(item) },
                shape = SegmentedButtonDefaults.itemShape(index, periods.size),
                label = { Text(item.label()) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinDetailPreview() {
    val points = List(40) { PricePoint(it * 3_600_000L, 50000.0 + it * 120 + (it % 5) * 300) }
    CoinDetailScreen(
        state = CoinDetailUiState.Success(Coin("btc", "BTC", "Bitcoin", "", 50000.0, 2.2)),
        chartState = PriceChartUiState.Success(points),
        period = ChartPeriod.WEEK,
        onPeriodSelected = {},
        onChartRetry = {},
        onBackClick = {}
    )
}
