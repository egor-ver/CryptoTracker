package com.example.cryptotracker.ui.coinlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.components.CoinItem
import com.example.cryptotracker.ui.components.ErrorWithRetry
import com.example.cryptotracker.ui.components.LoadingBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    state: CoinListUiState,
    coins: List<Coin>,
    favorites: Set<String>,
    query: String,
    onQueryChange: (String) -> Unit,
    onCoinClick: (Coin) -> Unit,
    onToggleFavorite: (Coin) -> Unit,
    onRetry: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Криптовалюты") }, actions = {
            IconButton(
                onFavoritesClick
            ) {
                Icon(Icons.Default.Star, "Избранное")
            }
        }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Поиск монеты") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (state) {
                is CoinListUiState.Success -> if (coins.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Ничего не найдено",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(coins, key = { it.id }){
                            item -> CoinItem(
                                coin = item,
                                isFavorite = item.id in favorites,
                                onClick = {onCoinClick(item)},
                                onToggleFavorite = {onToggleFavorite(item)}
                            )
                        }
                    }
                }

                is CoinListUiState.Loading -> LoadingBox(Modifier.fillMaxSize())

                is CoinListUiState.Error -> ErrorWithRetry(state.error, onRetry, Modifier.fillMaxSize())
            }
        }
    }
}
