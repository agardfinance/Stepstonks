package com.stepstonks.app.data.local.database.dao

import androidx.room.*
import com.stepstonks.app.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET xp = xp + :xp, totalSteps = totalSteps + :steps WHERE id = :id")
    suspend fun addXpAndSteps(id: String, xp: Long, steps: Long)

    @Query("UPDATE users SET tokenBalance = tokenBalance + :amount, totalTokensEarned = totalTokensEarned + :amount WHERE id = :id AND :amount > 0")
    suspend fun addTokens(id: String, amount: Double)

    @Query("UPDATE users SET tokenBalance = tokenBalance - :amount WHERE id = :id AND tokenBalance >= :amount")
    suspend fun spendTokens(id: String, amount: Double): Int

    @Query("UPDATE users SET energy = :energy, lastEnergyRefillAt = :timestamp WHERE id = :id")
    suspend fun updateEnergy(id: String, energy: Float, timestamp: Long)

    @Query("UPDATE users SET currentStreak = :streak, longestStreak = MAX(longestStreak, :streak) WHERE id = :id")
    suspend fun updateStreak(id: String, streak: Int)

    @Query("UPDATE users SET equippedSneakerId = :sneakerId, lastActiveAt = :timestamp WHERE id = :id")
    suspend fun equipSneaker(id: String, sneakerId: String?, timestamp: Long)

    @Query("UPDATE users SET level = :level, xpToNextLevel = :xpToNextLevel, maxEnergy = :maxEnergy WHERE id = :id")
    suspend fun updateLevel(id: String, level: Int, xpToNextLevel: Long, maxEnergy: Float)

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun countByUsername(username: String): Int
}
