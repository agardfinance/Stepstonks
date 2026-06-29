package com.stepstonks.app.presentation.screens.sneakers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SneakersUiState(
    val mySneakers: List<Sneaker> = emptyList(),
    val shopItems: List<Sneaker> = SNEAKER_SHOP_ITEMS,
    val equippedSneakerId: String? = null,
    val userTokenBalance: Double = 0.0,
    val selectedTab: Int = 0,
    val actionMessage: String? = null
)

@HiltViewModel
class SneakersViewModel @Inject constructor(
    private val sneakerRepository: SneakerRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SneakersUiState())
    val uiState: StateFlow<SneakersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                sneakerRepository.getUserSneakers(),
                userRepository.getCurrentUser()
            ) { sneakers, user ->
                SneakersUiState(
                    mySneakers = sneakers,
                    equippedSneakerId = user?.equippedSneakerId,
                    userTokenBalance = user?.tokenBalance ?: 0.0
                )
            }.collect { _uiState.value = it }
        }
    }

    fun equipSneaker(sneakerId: String) {
        viewModelScope.launch {
            sneakerRepository.equipSneaker(sneakerId)
            val user = userRepository.getCurrentUser().firstOrNull() ?: return@launch
            userRepository.equipSneaker(user.id, sneakerId)
            _uiState.update { it.copy(actionMessage = "Sneaker equipped!") }
        }
    }

    fun buySneaker(sneaker: Sneaker) {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser().firstOrNull() ?: return@launch
            val cost = sneaker.rarity.mintCost
            if (user.tokenBalance < cost) {
                _uiState.update { it.copy(actionMessage = "Not enough STK! Need ${cost.toInt()} STK") }
                return@launch
            }
            val purchased = sneaker.copy(ownerId = user.id, isEquipped = false)
            sneakerRepository.addSneaker(purchased)
            userRepository.spendTokens(user.id, cost)
            tokenRepository.recordTransaction(
                TokenTransaction(
                    type = TransactionType.SPENT_SNEAKER_PURCHASE,
                    amount = cost,
                    description = "Bought ${sneaker.name} (${sneaker.rarity.displayName})",
                    balanceAfter = user.tokenBalance - cost
                )
            )
            _uiState.update { it.copy(actionMessage = "${sneaker.name} purchased!") }
        }
    }

    fun upgradeSneaker(sneakerId: String) {
        viewModelScope.launch {
            val sneaker = sneakerRepository.getSneakerById(sneakerId) ?: return@launch
            val user = userRepository.getCurrentUser().firstOrNull() ?: return@launch
            val cost = (sneaker.level + 1) * 50.0
            if (user.tokenBalance < cost) {
                _uiState.update { it.copy(actionMessage = "Need ${cost.toInt()} STK to upgrade") }
                return@launch
            }
            val upgraded = sneakerRepository.upgradeSneaker(sneakerId)
            if (upgraded) {
                userRepository.spendTokens(user.id, cost)
                _uiState.update { it.copy(actionMessage = "Sneaker upgraded to Lv.${sneaker.level + 1}!") }
            }
        }
    }

    fun clearMessage() = _uiState.update { it.copy(actionMessage = null) }
    fun selectTab(tab: Int) = _uiState.update { it.copy(selectedTab = tab) }
}
