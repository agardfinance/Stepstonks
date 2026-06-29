package com.stepstonks.app.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.data.local.preferences.UserPreferencesDataStore
import com.stepstonks.app.domain.model.STARTER_SNEAKER
import com.stepstonks.app.domain.model.User
import com.stepstonks.app.domain.repository.ChallengeRepository
import com.stepstonks.app.domain.repository.SneakerRepository
import com.stepstonks.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val challengeRepository: ChallengeRepository,
    private val sneakerRepository: SneakerRepository,
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createAccount(username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                if (username.length < 3) {
                    _error.value = "Username must be at least 3 characters"
                    return@launch
                }
                val available = userRepository.isUsernameAvailable(username)
                if (!available) {
                    _error.value = "Username already taken"
                    return@launch
                }
                val userId = UUID.randomUUID().toString()
                val user = User(
                    id = userId,
                    username = username,
                    email = "",
                    referralCode = generateReferralCode(username)
                )
                userRepository.createUser(user)
                challengeRepository.initializeChallengesForUser(userId)
                val starterSneaker = STARTER_SNEAKER.copy(ownerId = userId, isEquipped = true)
                sneakerRepository.addSneaker(starterSneaker)
                sneakerRepository.equipSneaker(starterSneaker.id)
                prefs.setUserId(userId)
                prefs.setOnboarded(true)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to create account: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateReferralCode(username: String): String {
        val suffix = (1000..9999).random()
        return "${username.take(4).uppercase()}$suffix"
    }
}
