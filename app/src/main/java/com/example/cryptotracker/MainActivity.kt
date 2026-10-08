package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.list.CoinListScreen
import com.example.cryptotracker.ui.list.CoinDetailScreen
import com.example.cryptotracker.ui.list.CoinDetailViewModel
import com.example.cryptotracker.ui.list.CoinListViewModel
import com.example.cryptotracker.ui.list.FavoritesScreen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CoinListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                val navController = rememberNavController()
                NavHost(navController, "list") {
                    composable("list"){
                        val state by viewModel.uiState.collectAsStateWithLifecycle()
                        val coins by viewModel.filteredCoins.collectAsStateWithLifecycle()
                        val favorites by viewModel.favorites.collectAsStateWithLifecycle()
                        val query by viewModel.searchState.collectAsStateWithLifecycle()
                        CoinListScreen(
                            state = state,
                            coins = coins,
                            favorites = favorites,
                            query = query,
                            onQueryChange = viewModel::onQueryChanged,
                            onCoinClick = {coin ->
                                navController.navigate("detail/${coin.id}")
                            },
                            onToggleFavorite = {coin -> viewModel.editFavorite(coin.id)},
                            onRetry = viewModel::retry,
                            onFavoritesClick = {navController.navigate("favorites")}
                        )
                    }
                    composable(
                        "detail/{coinId}",
                        listOf(navArgument("coinId"){
                            type = NavType.StringType
                        })

                    ){
                        val detailViewModel: CoinDetailViewModel = hiltViewModel()
                        val detailState by detailViewModel.uiState.collectAsStateWithLifecycle()
                        CoinDetailScreen(detailState, onBackClick = { navController.popBackStack() })
                    }
                    composable("favorites"){
                        val state by viewModel.uiState.collectAsStateWithLifecycle()
                        val favorites by viewModel.favoriteCoins.collectAsStateWithLifecycle()
                        FavoritesScreen(
                            state = state,
                            favorites = favorites,
                            onBackClick = {navController.popBackStack()},
                            onCoinClick = {coin -> navController.navigate("detail/${coin.id}")},
                            onToggleFavorite = {coin -> viewModel.editFavorite(coin.id)},
                            onRetry = viewModel::retry
                        )
                    }
                }

            }
        }
    }
}

