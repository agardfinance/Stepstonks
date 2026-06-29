package com.stepstonks.app.presentation.screens.walk

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import com.stepstonks.app.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalkUiState(
    val user: User? = null,
    val todaySteps: StepData? = null,
    val equippedSneaker: Sneaker? = null,
    val isTracking: Boolean = false,
    val sessionTokens: Double = 0.0,
    val sessionXp: Long = 0L
)

@HiltViewModel
class WalkViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val stepRepository: StepRepository,
    private val sneakerRepository: SneakerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    private val _isTracking = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            combine(
                userRepository.getCurrentUser(),
                stepRepository.getTodaySteps(),
                sneakerRepository.getEquippedSneaker(),
                _isTracking
            ) { user, steps, sneaker, tracking ->
                WalkUiState(
                    user = user,
                    todaySteps = steps,
                    equippedSneaker = sneaker,
                    isTracking = tracking
                )
            }.collect { _uiState.value = it }
        }
    }

    fun startWalking() {
        _isTracking.value = true
        StepCounterService.startService(context)
    }

    fun stopWalking() {
        _isTracking.value = false
        StepCounterService.stopService(context)
    }

    fun getCurrentEarningRate(): Double {
        val user = _uiState.value.user ?: return 0.01
        val sneaker = _uiState.value.equippedSneaker
        val userMultiplier = user.earningMultiplier()
        val sneakerMultiplier = sneaker?.earningRate ?: 1.0
        return 0.01 * userMultiplier * sneakerMultiplier
    }

    override fun onCleared() {
        super.onCleared()
        if (_isTracking.value) stopWalking()
    }
}
