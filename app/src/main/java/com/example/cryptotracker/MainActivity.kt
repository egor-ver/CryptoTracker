package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.CoinItem
import com.example.cryptotracker.ui.CoinListScreen
import com.example.cryptotracker.ui.list.CoinDetailScreen
import com.example.cryptotracker.ui.list.CoinListUiState
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
                                navController.navigate("detail/${coin.id}")}
                        )
                    }
                    composable(
                        "detail/{coinId}",
                        listOf(navArgument("coinId"){
                            type = NavType.StringType
                        })

                    ){
                        backStackEntry ->
                        val coinId = backStackEntry.arguments?.getString("coinId")
                        val uiState by viewModel.uiState.collectAsState()
                        val coin = (uiState as CoinListUiState.Success)?.coins?.find { it.id == coinId }
                        if(coin != null){
                            CoinDetailScreen(coin)
                        }
                        else CircularProgressIndicator()
                    }
                }

            }
        }
    }
}

