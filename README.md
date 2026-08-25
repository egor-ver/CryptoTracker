# CryptoTracker

A cryptocurrency price tracker for Android — browse live coin prices and 24h changes, tap into any coin for details. Built to practice modern Android architecture end-to-end: Jetpack Compose UI, MVVM with a clean data/domain/ui separation, coroutines + Flow, and a real REST API.

## Features

- 📈 Live list of cryptocurrencies with current price and 24h change (color-coded: green up / red down)
- 🔍 Tap a coin to open a detail screen
- ⏳ Loading / error states with a **Retry** action on failure
- 🔄 Data fetched from the [CoinGecko](https://www.coingecko.com/en/api) public API

## Tech stack

| Area | Tools |
|------|-------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, unidirectional data flow (UDF) |
| Async | Coroutines, Flow, StateFlow |
| Networking | Retrofit, OkHttp, Gson |
| Navigation | Navigation-Compose |
| Testing | JUnit, coroutines-test (fake repository, MainDispatcherRule) |

## Architecture

Clean separation into three layers, dependencies pointing inward:

```
ui (Compose screens + ViewModel, StateFlow<UiState>)
        ↓ observes state / sends events
domain (Coin model)
        ↓
data (Retrofit CoinApi, DTOs, mapper, Repository)
        ↓
CoinGecko REST API
```

**Data flow:** the UI collects `StateFlow<UiState>` from the ViewModel and renders it; user actions call ViewModel methods; the ViewModel asks the Repository, which fetches DTOs from the network, maps them to domain models, and returns them — the ViewModel updates its state, and the UI recomposes.

- **data** — `CoinApi` (Retrofit interface), `RetrofitClient`, `CoinDto`, `CoinMappers`, `CoinRepository` + `CoinRepositoryImpl`
- **domain** — `Coin` (clean model)
- **ui** — `CoinListScreen` / `CoinListViewModel` / `CoinListUiState`, `CoinDetailScreen`, `MainActivity` (NavHost)

## Testing

Unit tests run on the JVM (no device needed):

- **Mapper test** — verifies DTO → domain mapping field-by-field
- **ViewModel test** — a `FakeRepository` injected via constructor + `MainDispatcherRule` to drive coroutines in tests

## Getting started

1. Clone the repo and open in Android Studio.
2. Run on an emulator or device (min SDK 24).

The API is public and needs no key.
