package com.example.cryptotracker.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cryptotracker.domain.model.Coin

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
                    TextButton(
                        onClick = onBackClick
                    ) {
                        Text("Назад")
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
                    Text("Пока нет избранных монет")
                }
            } else{
                LazyColumn(modifier) {
                    items(favorites){ coin ->
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
