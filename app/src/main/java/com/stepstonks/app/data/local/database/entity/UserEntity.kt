package com.stepstonks.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepstonks.app.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
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

fun UserEntity.toDomain() = User(
    id, username, email, avatarUrl, level, xp, xpToNextLevel,
    totalSteps, totalTokensEarned, tokenBalance, gemBalance,
    currentStreak, longestStreak, energy, maxEnergy,
    equippedSneakerId, referralCode, referralCount, squadId,
    joinedAt, lastActiveAt, lastEnergyRefillAt
)

fun User.toEntity() = UserEntity(
    id, username, email, avatarUrl, level, xp, xpToNextLevel,
    totalSteps, totalTokensEarned, tokenBalance, gemBalance,
    currentStreak, longestStreak, energy, maxEnergy,
    equippedSneakerId, referralCode, referralCount, squadId,
    joinedAt, lastActiveAt, lastEnergyRefillAt
)
