package com.stepstonks.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepstonks.app.domain.model.StepData

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey val date: String,
    val stepCount: Long = 0L,
    val tokensEarned: Double = 0.0,
    val xpEarned: Long = 0L,
    val caloriesBurned: Double = 0.0,
    val distanceKm: Double = 0.0,
    val activeMinutes: Int = 0,
    val goalSteps: Long = 10_000L,
    val createdAt: Long = System.currentTimeMillis()
)

fun StepEntity.toDomain() = StepData(
    date = date, stepCount = stepCount, tokensEarned = tokensEarned,
    xpEarned = xpEarned, caloriesBurned = caloriesBurned,
    distanceKm = distanceKm, activeMinutes = activeMinutes,
    goalSteps = goalSteps, createdAt = createdAt
)

fun StepData.toEntity() = StepEntity(
    date = date, stepCount = stepCount, tokensEarned = tokensEarned,
    xpEarned = xpEarned, caloriesBurned = caloriesBurned,
    distanceKm = distanceKm, activeMinutes = activeMinutes,
    goalSteps = goalSteps, createdAt = createdAt
)
