package com.example.cryptotracker.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.theme.PriceUp
import com.example.cryptotracker.ui.theme.PriceDown
@Composable
fun CoinDetailScreen(coin: Coin, onBackClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = coin.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = coin.symbol,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text("Цена: ${"%.2f".format(coin.price)}$")
                Spacer(
                    Modifier.height(8.dp)
                )
                val priceColor = when{
                    coin.priceChange > 0 -> PriceUp
                    coin.priceChange < 0 -> PriceDown
                    else -> Color.Gray
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Изменение за 24ч:")
                    Text(
                        text = "${"%.2f".format(coin.priceChange)}%",
                        color = priceColor
                    )
                }


            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {onBackClick()}) {
            Text(
                text = "Назад",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinDetailPreview(){
    CoinDetailScreen(Coin("btc", "BTC", "Bitcoin", "", 50000.0, 2.2), onBackClick = {})
}