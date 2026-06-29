package com.stepstonks.app.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeaderboardUiState(
    val entries: List<LeaderboardEntry> = emptyList(),
    val selectedPeriod: LeaderboardPeriod = LeaderboardPeriod.DAILY,
    val currentUser: User? = null,
    val todaySteps: StepData? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stepRepository: StepRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                stepRepository.getTodaySteps()
            ) { user, steps ->
                Pair(user, steps)
            }.collect { (user, steps) ->
                if (user != null) {
                    val leaderboard = generateMockLeaderboard(
                        currentUserId = user.id,
                        currentUsername = user.username,
                        currentSteps = steps?.stepCount ?: 0L,
                        currentLevel = user.level
                    )
                    _uiState.update {
                        it.copy(
                            entries = leaderboard,
                            currentUser = user,
                            todaySteps = steps,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun selectPeriod(period: LeaderboardPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
    }
}
