package com.stepstonks.app.data.local.database.dao

import androidx.room.*
import com.stepstonks.app.data.local.database.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE status != 'LOCKED' ORDER BY type, status")
    fun getActiveChallenges(): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE type = :type ORDER BY status")
    fun getChallengesByType(type: String): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE type = 'ACHIEVEMENT' ORDER BY status, targetValue")
    fun getAchievements(): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getChallengeById(id: String): ChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallenge(challenge: ChallengeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallenges(challenges: List<ChallengeEntity>)

    @Update
    suspend fun updateChallenge(challenge: ChallengeEntity)

    @Query("UPDATE challenges SET currentValue = :value WHERE id = :id AND status = 'ACTIVE'")
    suspend fun updateProgress(id: String, value: Long)

    @Query("UPDATE challenges SET status = 'COMPLETED', completedAt = :timestamp WHERE id = :id")
    suspend fun markCompleted(id: String, timestamp: Long)

    @Query("UPDATE challenges SET status = 'CLAIMED' WHERE id = :id")
    suspend fun markClaimed(id: String)

    @Query("UPDATE challenges SET currentValue = 0, status = 'ACTIVE', completedAt = NULL WHERE type = 'DAILY'")
    suspend fun resetDailyChallenges()

    @Query("UPDATE challenges SET currentValue = 0, status = 'ACTIVE', completedAt = NULL WHERE type = 'WEEKLY'")
    suspend fun resetWeeklyChallenges()

    @Query("SELECT COUNT(*) FROM challenges WHERE status = 'CLAIMED'")
    fun getClaimedCount(): Flow<Int>
}
