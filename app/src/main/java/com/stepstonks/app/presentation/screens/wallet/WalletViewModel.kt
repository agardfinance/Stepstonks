package com.stepstonks.app.presentation.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.data.remote.CoinGeckoApiService
import com.stepstonks.app.data.remote.dto.toDomain
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class WalletUiState(
    val user: User? = null,
    val recentTransactions: List<TokenTransaction> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalSpent: Double = 0.0,
    val cryptoAssets: List<CryptoAsset> = emptyList(),
    val stkPrice: Double = 0.01,
    val stkPriceChange: Double = 2.5,
    val isLoadingCrypto: Boolean = true,
    val cryptoError: Boolean = false,
    val searchQuery: String = "",
    val lastUpdated: Long = 0L,
    val isWalletLoading: Boolean = true
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val coinGeckoApi: CoinGeckoApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    val filteredAssets: StateFlow<List<CryptoAsset>> = combine(
        _uiState.map { it.cryptoAssets },
        _uiState.map { it.searchQuery }
    ) { assets, query ->
        if (query.isBlank()) assets
        else assets.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.symbol.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadWalletData()
        startPriceRefreshLoop()
    }

    private fun loadWalletData() {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                tokenRepository.getRecentTransactions(20),
            ) { user, transactions ->
                _uiState.update { it.copy(user = user, recentTransactions = transactions, isWalletLoading = false) }
            }.collect()
        }
        viewModelScope.launch {
            val earned = tokenRepository.getTotalEarned()
            val spent = tokenRepository.getTotalSpent()
            _uiState.update { it.copy(totalEarned = earned, totalSpent = spent) }
        }
    }

    private fun startPriceRefreshLoop() {
        viewModelScope.launch {
            while (true) {
                fetchPrices()
                delay(30_000L)
            }
        }
    }

    private suspend fun fetchPrices() {
        try {
            val coins = coinGeckoApi.getMarkets()
            val assets = coins.map { it.toDomain() }
            val stkChange = _uiState.value.stkPriceChange + Random.nextDouble(-0.3, 0.3)
            val stkPrice = 0.01 * (1 + Random.nextDouble(-0.002, 0.002))
            _uiState.update {
                it.copy(
                    cryptoAssets = assets,
                    stkPrice = stkPrice,
                    stkPriceChange = stkChange.coerceIn(-15.0, 25.0),
                    isLoadingCrypto = false,
                    cryptoError = false,
                    lastUpdated = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            // Fallback to built-in data with slight random variation
            val fallback = FALLBACK_CRYPTO.filter { !it.isSTK }.map { asset ->
                asset.copy(
                    currentPrice = asset.currentPrice * (1 + Random.nextDouble(-0.015, 0.015)),
                    priceChange24h = asset.priceChange24h + Random.nextDouble(-0.5, 0.5)
                )
            }
            val stkPrice = 0.01 * (1 + Random.nextDouble(-0.002, 0.002))
            val stkChange = _uiState.value.stkPriceChange + Random.nextDouble(-0.3, 0.3)
            _uiState.update {
                it.copy(
                    cryptoAssets = fallback,
                    stkPrice = stkPrice,
                    stkPriceChange = stkChange.coerceIn(-15.0, 25.0),
                    isLoadingCrypto = false,
                    cryptoError = true,
                    lastUpdated = System.currentTimeMillis()
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun refreshPrices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCrypto = true) }
            fetchPrices()
        }
    }
}
