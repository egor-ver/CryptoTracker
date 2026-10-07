package com.example.cryptotracker.ui.list

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.cryptotracker.R

// ViewModel отдаёт тип ошибки, а текст выбирает UI: так ViewModel не зависит от строк и локализации.
enum class LoadError(@StringRes val messageRes: Int) {
    NETWORK(R.string.error_network),
    SERVER(R.string.error_server),
    NOT_FOUND(R.string.error_not_found)
}

@Composable
fun LoadError.message(): String = stringResource(messageRes)
