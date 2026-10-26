package com.example.cryptotracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.domain.model.ChartPeriod
import com.example.cryptotracker.domain.model.PricePoint
import com.example.cryptotracker.ui.common.formatChartDate
import com.example.cryptotracker.ui.common.formatPrice
import com.example.cryptotracker.ui.common.percentChange
import com.example.cryptotracker.ui.theme.PriceDown
import com.example.cryptotracker.ui.theme.PriceUp
import kotlin.math.roundToInt

private val ChartHeight = 200.dp
private val ChartPadding = 10.dp

// Линия окрашена по итогу периода: выросла цена к концу или упала. Палец на графике показывает цену и дату точки.
@Composable
fun PriceChart(points: List<PricePoint>, period: ChartPeriod, modifier: Modifier = Modifier) {
    if (points.size < 2) return
    val color = if (points.last().price >= points.first().price) PriceUp else PriceDown
    val guideColor = MaterialTheme.colorScheme.onSurfaceVariant
    val dotCenterColor = MaterialTheme.colorScheme.surface
    val minPrice = points.minOf { it.price }
    val maxPrice = points.maxOf { it.price }
    // Если цена не менялась, диапазон нулевой: делим на 1, и линия идёт ровно по центру
    val range = (maxPrice - minPrice).takeIf { it > 0 } ?: 1.0

    var selectedIndex by remember(points) { mutableStateOf<Int?>(null) }
    val shown = points[selectedIndex ?: points.lastIndex]

    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatPrice(shown.price),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (selectedIndex == null) "Сейчас" else formatChartDate(shown.timestamp, period),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Изменение от начала периода до выбранной точки, без выбора: за весь период
            ChangePill(percentChange(points.first().price, shown.price))
        }
        Spacer(Modifier.height(12.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(ChartHeight)
                .pointerInput(points) {
                    detectTapGestures(
                        onPress = { offset ->
                            selectedIndex = indexAt(offset.x, size.width.toFloat(), points.size)
                            tryAwaitRelease()
                            selectedIndex = null
                        }
                    )
                }
                .pointerInput(points) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            selectedIndex = indexAt(offset.x, size.width.toFloat(), points.size)
                        },
                        onDragEnd = { selectedIndex = null },
                        onDragCancel = { selectedIndex = null },
                        onHorizontalDrag = { change, _ ->
                            selectedIndex = indexAt(change.position.x, size.width.toFloat(), points.size)
                        }
                    )
                }
        ) {
            val padding = ChartPadding.toPx()
            val usableHeight = size.height - padding * 2
            fun xAt(index: Int) = size.width * index / points.lastIndex
            fun yAt(price: Double) = (size.height - padding - ((price - minPrice) / range * usableHeight)).toFloat()

            val line = Path().apply {
                points.forEachIndexed { index, point ->
                    if (index == 0) moveTo(xAt(index), yAt(point.price))
                    else lineTo(xAt(index), yAt(point.price))
                }
            }
            val area = Path().apply {
                addPath(line)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = area,
                brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.28f), Color.Transparent))
            )
            drawPath(
                path = line,
                color = color,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            selectedIndex?.let { index ->
                val x = xAt(index)
                val y = yAt(points[index].price)
                drawLine(
                    color = guideColor.copy(alpha = 0.4f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawCircle(color = color, radius = 6.dp.toPx(), center = Offset(x, y))
                drawCircle(color = dotCenterColor, radius = 3.dp.toPx(), center = Offset(x, y))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Мин ${formatPrice(minPrice)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Макс ${formatPrice(maxPrice)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Ближайшая к пальцу точка. Касание может выйти за край графика, поэтому зажимаем в границы.
private fun indexAt(x: Float, width: Float, count: Int): Int {
    if (width <= 0f) return 0
    return (x / width * (count - 1)).roundToInt().coerceIn(0, count - 1)
}
