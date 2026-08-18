package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptotracker.ui.CoinItem
import com.example.cryptotracker.ui.CoinListScreen
import com.example.cryptotracker.ui.list.CoinDetailScreen
import com.example.cryptotracker.ui.list.CoinListViewModel
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

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
                                viewModel.selectCoin(coin)
                                navController.navigate("detail")}
                        )
                    }
                    composable("detail"){
                        viewModel.selectedCoin?.let{coin -> CoinDetailScreen(coin) }
                    }
                }

            }
        }
    }
}

