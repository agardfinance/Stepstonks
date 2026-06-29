package com.stepstonks.app.domain.model

data class StepData(
    val id: Long = 0,
    val date: String,
    val stepCount: Long = 0L,
    val tokensEarned: Double = 0.0,
    val xpEarned: Long = 0L,
    val caloriesBurned: Double = 0.0,
    val distanceKm: Double = 0.0,
    val activeMinutes: Int = 0,
    val goalSteps: Long = 10_000L,
    val createdAt: Long = System.currentTimeMillis()
) {
    val goalProgressPercent: Float
        get() = if (goalSteps == 0L) 1f else (stepCount.toFloat() / goalSteps.toFloat()).coerceIn(0f, 1f)

    val isGoalCompleted: Boolean
        get() = stepCount >= goalSteps
}

fun Long.toCalories(): Double = this * 0.04
fun Long.toDistanceKm(): Double = this * 0.000762
fun Long.toActiveMinutes(): Int = (this / 100).toInt()
