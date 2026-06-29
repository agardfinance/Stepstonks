package com.stepstonks.app.data.local.database.dao

import androidx.room.*
import com.stepstonks.app.data.local.database.entity.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Query("SELECT * FROM steps WHERE date = :date")
    fun getStepsByDate(date: String): Flow<StepEntity?>

    @Query("SELECT * FROM steps WHERE date = :date")
    suspend fun getStepsByDateSync(date: String): StepEntity?

    @Query("SELECT * FROM steps ORDER BY date DESC LIMIT :days")
    fun getRecentSteps(days: Int): Flow<List<StepEntity>>

    @Query("SELECT * FROM steps ORDER BY date DESC LIMIT 7")
    fun getWeeklySteps(): Flow<List<StepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stepEntity: StepEntity)

    @Query("UPDATE steps SET stepCount = :steps, tokensEarned = tokensEarned + :tokens, xpEarned = xpEarned + :xp, caloriesBurned = :calories, distanceKm = :distance WHERE date = :date")
    suspend fun updateSteps(date: String, steps: Long, tokens: Double, xp: Long, calories: Double, distance: Double)

    @Query("SELECT COALESCE(SUM(stepCount), 0) FROM steps")
    suspend fun getTotalSteps(): Long

    @Query("SELECT * FROM steps ORDER BY date DESC LIMIT :limit")
    fun getStepsHistory(limit: Int): Flow<List<StepEntity>>
}
