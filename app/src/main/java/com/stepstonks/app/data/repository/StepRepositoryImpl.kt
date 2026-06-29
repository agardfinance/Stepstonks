package com.stepstonks.app.data.repository

import com.stepstonks.app.data.local.database.dao.StepDao
import com.stepstonks.app.data.local.database.entity.StepEntity
import com.stepstonks.app.data.local.database.entity.toDomain
import com.stepstonks.app.data.local.database.entity.toEntity
import com.stepstonks.app.domain.model.StepData
import com.stepstonks.app.domain.model.toCalories
import com.stepstonks.app.domain.model.toDistanceKm
import com.stepstonks.app.domain.repository.StepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepRepositoryImpl @Inject constructor(
    private val stepDao: StepDao
) : StepRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private fun today() = dateFormat.format(Date())

    override fun getTodaySteps(): Flow<StepData?> =
        stepDao.getStepsByDate(today()).map { it?.toDomain() }

    override fun getStepsHistory(days: Int): Flow<List<StepData>> =
        stepDao.getStepsHistory(days).map { list -> list.map { it.toDomain() } }

    override fun getWeeklySteps(): Flow<List<StepData>> =
        stepDao.getWeeklySteps().map { list -> list.map { it.toDomain() } }

    override suspend fun saveStepData(stepData: StepData) =
        stepDao.insertOrUpdate(stepData.toEntity())

    override suspend fun updateTodaySteps(steps: Long, tokensEarned: Double, xpEarned: Long) {
        val date = today()
        val existing = stepDao.getStepsByDateSync(date)
        if (existing == null) {
            stepDao.insertOrUpdate(
                StepEntity(
                    date = date,
                    stepCount = steps,
                    tokensEarned = tokensEarned,
                    xpEarned = xpEarned,
                    caloriesBurned = steps.toCalories(),
                    distanceKm = steps.toDistanceKm()
                )
            )
        } else {
            stepDao.updateSteps(date, steps, tokensEarned, xpEarned, steps.toCalories(), steps.toDistanceKm())
        }
    }

    override suspend fun getTotalSteps(): Long = stepDao.getTotalSteps()

    override suspend fun getStepsForDate(date: String): StepData? =
        stepDao.getStepsByDateSync(date)?.toDomain()
}
