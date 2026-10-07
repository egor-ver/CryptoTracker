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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotracker.domain.model.Coin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: CoinListViewModel, onBackClick: () -> Unit, onCoinClick: (Coin) -> Unit){
    val favorites by viewModel.favoriteCoins.collectAsStateWithLifecycle()
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
        if(favorites.isEmpty()){
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                Text("Пока нет избранных монет")
            }
        } else{
            LazyColumn(Modifier.fillMaxSize().padding(innerPadding)) {
                items(favorites){ coin ->
                    CoinItem(
                        coin = coin,
                        isFavorite = true,
                        onClick = {onCoinClick(coin)},
                        onToggleFavorite = {viewModel.editFavorite((coin.id))}
                    )
                }
            }
        }

    }
}




