package com.example.cryptotracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.cryptotracker.data.remote.RetrofitClient
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            try{
               val coins = RetrofitClient.api.getCoins()
                Log.d("CRYPTO", "Загружено монет: ${coins.size}, первая: ${coins.first().name}")
            }
            catch (e: Exception){
                Log.e("CRYPTO", "Ошибка: ${e.message}")
            }
        }
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
            }

        }
    }
}

