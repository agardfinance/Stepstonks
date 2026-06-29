package com.stepstonks.app.presentation.screens.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengesUiState(
    val dailyChallenges: List<Challenge> = emptyList(),
    val weeklyChallenges: List<Challenge> = emptyList(),
    val specialChallenges: List<Challenge> = emptyList(),
    val claimResult: ClaimResult? = null,
    val isLoading: Boolean = false
)

sealed class ClaimResult {
    data class Success(val challenge: Challenge) : ClaimResult()
    data class Error(val message: String) : ClaimResult()
}

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                challengeRepository.getChallengesByType(ChallengeType.DAILY),
                challengeRepository.getChallengesByType(ChallengeType.WEEKLY),
                challengeRepository.getChallengesByType(ChallengeType.SPECIAL)
            ) { daily, weekly, special ->
                ChallengesUiState(
                    dailyChallenges = daily,
                    weeklyChallenges = weekly,
                    specialChallenges = special
                )
            }.collect { _uiState.value = it }
        }
    }

    fun claimChallenge(challengeId: String) {
        viewModelScope.launch {
            val claimed = challengeRepository.claimChallenge(challengeId)
            if (claimed != null) {
                val user = userRepository.getCurrentUser().firstOrNull()
                if (user != null) {
                    if (claimed.tokenReward > 0) {
                        userRepository.addTokens(user.id, claimed.tokenReward)
                        tokenRepository.recordTransaction(
                            TokenTransaction(
                                type = TransactionType.EARNED_CHALLENGE,
                                amount = claimed.tokenReward,
                                description = "Challenge: ${claimed.title}",
                                balanceAfter = user.tokenBalance + claimed.tokenReward,
                                referenceId = claimed.id
                            )
                        )
                    }
                    if (claimed.xpReward > 0) {
                        userRepository.addXp(user.id, claimed.xpReward)
                    }
                }
                _uiState.update { it.copy(claimResult = ClaimResult.Success(claimed)) }
            } else {
                _uiState.update { it.copy(claimResult = ClaimResult.Error("Cannot claim this challenge")) }
            }
        }
    }

    fun clearClaimResult() = _uiState.update { it.copy(claimResult = null) }
}
