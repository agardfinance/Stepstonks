package com.stepstonks.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val totalSteps: Long = 0L,
    val totalTokensEarned: Double = 0.0,
    val equippedSneaker: Sneaker? = null,
    val sneakerCount: Int = 0,
    val claimedAchievements: Int = 0,
    val completedChallenges: Int = 0,
    val currentLevel: Level = getLevel(1),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stepRepository: StepRepository,
    private val sneakerRepository: SneakerRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                sneakerRepository.getEquippedSneaker(),
                sneakerRepository.getUserSneakers(),
                challengeRepository.getClaimedChallengesCount()
            ) { user, sneaker, allSneakers, claimedCount ->
                ProfileUiState(
                    user = user,
                    totalSteps = user?.totalSteps ?: 0L,
                    totalTokensEarned = user?.totalTokensEarned ?: 0.0,
                    equippedSneaker = sneaker,
                    sneakerCount = allSneakers.size,
                    claimedAchievements = claimedCount,
                    currentLevel = getLevel(user?.level ?: 1),
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }
}
