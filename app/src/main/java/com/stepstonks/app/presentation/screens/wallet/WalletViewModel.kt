package com.stepstonks.app.presentation.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val user: User? = null,
    val recentTransactions: List<TokenTransaction> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalSpent: Double = 0.0,
    val dailyEarnings: List<Pair<String, Double>> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                tokenRepository.getRecentTransactions(20),
                tokenRepository.getDailyEarnings(7)
            ) { user, transactions, dailyEarnings ->
                WalletUiState(
                    user = user,
                    recentTransactions = transactions,
                    dailyEarnings = dailyEarnings,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
        viewModelScope.launch {
            val earned = tokenRepository.getTotalEarned()
            val spent = tokenRepository.getTotalSpent()
            _uiState.update { it.copy(totalEarned = earned, totalSpent = spent) }
        }
    }
}
