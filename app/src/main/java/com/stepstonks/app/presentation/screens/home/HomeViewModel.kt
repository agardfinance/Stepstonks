package com.stepstonks.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val user: User? = null,
    val todaySteps: StepData? = null,
    val currentLevel: Level = getLevel(1),
    val equippedSneaker: Sneaker? = null,
    val activeDailyChallengesCount: Int = 0,
    val completedChallengesCount: Int = 0,
    val weeklySteps: List<StepData> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stepRepository: StepRepository,
    private val challengeRepository: ChallengeRepository,
    private val sneakerRepository: SneakerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                stepRepository.getTodaySteps(),
                challengeRepository.getChallengesByType(ChallengeType.DAILY),
                sneakerRepository.getEquippedSneaker(),
                stepRepository.getWeeklySteps()
            ) { user, today, dailyChallenges, sneaker, weekly ->
                val level = getLevel(user?.level ?: 1)
                HomeUiState(
                    user = user,
                    todaySteps = today,
                    currentLevel = level,
                    equippedSneaker = sneaker,
                    activeDailyChallengesCount = dailyChallenges.count { it.status == ChallengeStatus.ACTIVE },
                    completedChallengesCount = dailyChallenges.count { it.status == ChallengeStatus.COMPLETED },
                    weeklySteps = weekly,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }
}
