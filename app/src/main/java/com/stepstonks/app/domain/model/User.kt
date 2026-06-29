package com.stepstonks.app.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val level: Int = 1,
    val xp: Long = 0L,
    val xpToNextLevel: Long = 500L,
    val totalSteps: Long = 0L,
    val totalTokensEarned: Double = 0.0,
    val tokenBalance: Double = 0.0,
    val gemBalance: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val energy: Float = 20f,
    val maxEnergy: Float = 20f,
    val equippedSneakerId: String? = null,
    val referralCode: String = "",
    val referralCount: Int = 0,
    val squadId: String? = null,
    val joinedAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis(),
    val lastEnergyRefillAt: Long = System.currentTimeMillis()
)

fun User.earningMultiplier(): Double {
    val levelBonus = 1.0 + (level - 1) * 0.02
    val streakBonus = 1.0 + (currentStreak.coerceAtMost(10) * 0.05)
    return levelBonus * streakBonus
}

fun User.xpProgressPercent(): Float =
    if (xpToNextLevel == 0L) 1f else (xp.toFloat() / xpToNextLevel.toFloat()).coerceIn(0f, 1f)

fun User.energyPercent(): Float =
    if (maxEnergy == 0f) 0f else (energy / maxEnergy).coerceIn(0f, 1f)
