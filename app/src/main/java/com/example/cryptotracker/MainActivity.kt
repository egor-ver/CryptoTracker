package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.list.CoinListScreen
import com.example.cryptotracker.ui.list.CoinDetailScreen
import com.example.cryptotracker.ui.list.CoinDetailUiState
import com.example.cryptotracker.ui.list.CoinDetailViewModel
import com.example.cryptotracker.ui.list.CoinListViewModel
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
                                navController.navigate("detail/${coin.id}")}
                        )
                    }
                    composable(
                        "detail/{coinId}",
                        listOf(navArgument("coinId"){
                            type = NavType.StringType
                        })

                    ){
                        val detailViewModel: CoinDetailViewModel = hiltViewModel()
                        val detailState by detailViewModel.uiState.collectAsState()
                        when (val s = detailState) {
                            is CoinDetailUiState.Success ->
                                CoinDetailScreen(s.coin, onBackClick = { navController.popBackStack() },)
                            is CoinDetailUiState.Loading ->
                                CircularProgressIndicator()
                            is CoinDetailUiState.Error ->
                                Text(s.message)
                        }
                    }
                }

            }
        }
    }
}

