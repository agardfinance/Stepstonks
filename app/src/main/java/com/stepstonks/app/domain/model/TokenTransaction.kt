package com.stepstonks.app.domain.model

enum class TransactionType {
    EARNED_WALKING,
    EARNED_CHALLENGE,
    EARNED_ACHIEVEMENT,
    EARNED_REFERRAL,
    EARNED_STREAK_BONUS,
    SPENT_SNEAKER_PURCHASE,
    SPENT_SNEAKER_UPGRADE,
    SPENT_ENERGY_REFILL,
    SPENT_MYSTERY_BOX,
    WITHDRAWN,
    DEPOSITED
}

data class TokenTransaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val balanceAfter: Double = 0.0,
    val referenceId: String? = null
) {
    val isEarning: Boolean get() = type.name.startsWith("EARNED")
    val isSpending: Boolean get() = type.name.startsWith("SPENT")
    val displayAmount: String get() = if (isEarning) "+${String.format("%.2f", amount)} STK" else "-${String.format("%.2f", amount)} STK"
}
