package com.stepstonks.app.presentation.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AchievementsUiState(
    val achievements: List<Challenge> = emptyList(),
    val claimedCount: Int = 0,
    val totalCount: Int = 0
)

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            challengeRepository.getAchievements().collect { achievements ->
                _uiState.update {
                    it.copy(
                        achievements = achievements,
                        claimedCount = achievements.count { a -> a.status == ChallengeStatus.CLAIMED },
                        totalCount = achievements.size
                    )
                }
            }
        }
    }

    fun claimAchievement(achievementId: String) {
        viewModelScope.launch {
            val claimed = challengeRepository.claimChallenge(achievementId) ?: return@launch
            val user = userRepository.getCurrentUser().firstOrNull() ?: return@launch
            if (claimed.tokenReward > 0) {
                userRepository.addTokens(user.id, claimed.tokenReward)
                tokenRepository.recordTransaction(
                    TokenTransaction(
                        type = TransactionType.EARNED_ACHIEVEMENT,
                        amount = claimed.tokenReward,
                        description = "Achievement: ${claimed.title}",
                        balanceAfter = user.tokenBalance + claimed.tokenReward,
                        referenceId = claimed.id
                    )
                )
            }
            if (claimed.xpReward > 0) {
                userRepository.addXp(user.id, claimed.xpReward)
            }
        }
    }
}
