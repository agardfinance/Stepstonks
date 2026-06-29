package com.stepstonks.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepstonks.app.domain.model.Challenge
import com.stepstonks.app.domain.model.ChallengeCategory
import com.stepstonks.app.domain.model.ChallengeStatus
import com.stepstonks.app.domain.model.ChallengeType

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val type: String,
    val category: String,
    val status: String = ChallengeStatus.ACTIVE.name,
    val targetValue: Long,
    val currentValue: Long = 0L,
    val tokenReward: Double = 0.0,
    val xpReward: Long = 0L,
    val gemReward: Int = 0,
    val iconEmoji: String = "🏃",
    val startsAt: Long = System.currentTimeMillis(),
    val endsAt: Long? = null,
    val completedAt: Long? = null,
    val requiredLevel: Int = 1
)

fun ChallengeEntity.toDomain() = Challenge(
    id = id, title = title, description = description,
    type = ChallengeType.valueOf(type), category = ChallengeCategory.valueOf(category),
    status = ChallengeStatus.valueOf(status), targetValue = targetValue,
    currentValue = currentValue, tokenReward = tokenReward,
    xpReward = xpReward, gemReward = gemReward, iconEmoji = iconEmoji,
    startsAt = startsAt, endsAt = endsAt, completedAt = completedAt, requiredLevel = requiredLevel
)

fun Challenge.toEntity() = ChallengeEntity(
    id = id, title = title, description = description,
    type = type.name, category = category.name, status = status.name,
    targetValue = targetValue, currentValue = currentValue,
    tokenReward = tokenReward, xpReward = xpReward, gemReward = gemReward,
    iconEmoji = iconEmoji, startsAt = startsAt, endsAt = endsAt,
    completedAt = completedAt, requiredLevel = requiredLevel
)
