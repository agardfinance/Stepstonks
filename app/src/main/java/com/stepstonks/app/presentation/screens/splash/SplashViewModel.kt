package com.stepstonks.app.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SplashDestination { HOME, ONBOARDING }

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    init {
        viewModelScope.launch {
            delay(1800)
            val isOnboarded = prefs.isOnboarded.first()
            _destination.value = if (isOnboarded) SplashDestination.HOME else SplashDestination.ONBOARDING
        }
    }
}
