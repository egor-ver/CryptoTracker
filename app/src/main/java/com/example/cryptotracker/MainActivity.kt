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
                        CoinListScreen(
                            viewModel = viewModel,
                            onCoinClick = {coin ->
                                navController.navigate("detail/${coin.id}")
                            },
                            onFavoriteClick = {navController.navigate("favorites")}

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
                        FavoritesScreen(
                            viewModel = viewModel,
                            onBackClick = {navController.popBackStack()},
                            onCoinClick = {coin -> navController.navigate("detail/${coin.id}")}
                        )
                    }
                }

            }
        }
    }
}

