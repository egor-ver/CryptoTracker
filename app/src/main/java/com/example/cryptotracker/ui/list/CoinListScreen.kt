package com.example.cryptotracker.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.list.CoinListUiState
import com.example.cryptotracker.ui.list.CoinListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(viewModel: CoinListViewModel, onCoinClick: (Coin) -> Unit){
    val uiState by viewModel.uiState.collectAsState()
    val state = uiState
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Криптовалюты")})
        }
    ) { innerPadding -> Box(modifier = Modifier.padding(innerPadding)){
        when(state){
        is CoinListUiState.Success -> LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.coins){ coin ->
                CoinItem(coin, onClick = {onCoinClick(coin)})
            }
        }
        is CoinListUiState.Error -> Column(
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
            Text("Ошибка ${state.message}")
            Button({viewModel.retry()}) {
                Text("Повторить")
            }
        }
        is CoinListUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }}

    }
}

@Composable
fun CoinItem(coin: Coin, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {onClick()})
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = coin.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coin.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = coin.symbol,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${coin.price}",
                    fontWeight = FontWeight.Bold
                )
                val changeColor = when{
                    coin.priceChange > 0 -> Color.Green
                    coin.priceChange < 0 -> Color.Red
                    else -> Color.Gray
                }
                Text(
                    text = "${coin.priceChange}%",
                    color = changeColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinItemPreview() {
    CoinItem(
        Coin(
            id = "btc",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "",
            price = 50000.0,
            priceChange = 2.2
        ),
        onClick = {}
    )
}