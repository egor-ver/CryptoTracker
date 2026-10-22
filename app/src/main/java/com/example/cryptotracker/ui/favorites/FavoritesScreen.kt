package com.example.cryptotracker.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.domain.model.Coin
import com.example.cryptotracker.ui.coinlist.CoinListUiState
import com.example.cryptotracker.ui.components.CoinItem
import com.example.cryptotracker.ui.components.ErrorWithRetry
import com.example.cryptotracker.ui.components.LoadingBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    state: CoinListUiState,
    favorites: List<Coin>,
    onBackClick: () -> Unit,
    onCoinClick: (Coin) -> Unit,
    onToggleFavorite: (Coin) -> Unit,
    onRetry: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Избранное")},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.fillMaxSize().padding(innerPadding)
        // Избранное собирается из общего списка монет: пока он грузится или упал,
        // писать «Пока нет избранных» нельзя, избранные есть, просто ещё не пришли.
        when (state) {
            is CoinListUiState.Loading -> LoadingBox(modifier)
            is CoinListUiState.Error -> ErrorWithRetry(state.error, onRetry, modifier)
            is CoinListUiState.Success -> if(favorites.isEmpty()){
                Box(
                    modifier,
                    contentAlignment = Alignment.Center
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Пока нет избранных монет", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Нажми на звезду у монеты в списке",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else{
                LazyColumn(modifier, contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(favorites, key = { it.id }){ coin ->
                        CoinItem(
                            coin = coin,
                            isFavorite = true,
                            onClick = {onCoinClick(coin)},
                            onToggleFavorite = {onToggleFavorite(coin)}
                        )
                    }
                }
            }
        }
    }
}
