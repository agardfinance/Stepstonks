package com.stepstonks.app.domain.repository

import com.stepstonks.app.domain.model.StepData
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    fun getTodaySteps(): Flow<StepData?>
    fun getStepsHistory(days: Int): Flow<List<StepData>>
    fun getWeeklySteps(): Flow<List<StepData>>
    suspend fun saveStepData(stepData: StepData)
    suspend fun updateTodaySteps(steps: Long, tokensEarned: Double, xpEarned: Long)
    suspend fun getTotalSteps(): Long
    suspend fun getStepsForDate(date: String): StepData?
}
