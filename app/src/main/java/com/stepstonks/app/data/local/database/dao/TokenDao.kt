package com.stepstonks.app.data.local.database.dao

import androidx.room.*
import com.stepstonks.app.data.local.database.entity.TokenTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {
    @Query("SELECT * FROM token_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TokenTransactionEntity>>

    @Query("SELECT * FROM token_transactions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<TokenTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TokenTransactionEntity)

    @Query("SELECT COALESCE(SUM(amount), 0) FROM token_transactions WHERE type LIKE 'EARNED_%'")
    suspend fun getTotalEarned(): Double

    @Query("SELECT COALESCE(SUM(amount), 0) FROM token_transactions WHERE type LIKE 'SPENT_%'")
    suspend fun getTotalSpent(): Double

    @Query("SELECT date(timestamp/1000, 'unixepoch') as day, SUM(amount) as total FROM token_transactions WHERE type LIKE 'EARNED_%' GROUP BY day ORDER BY day DESC LIMIT :days")
    fun getDailyEarnings(days: Int): Flow<List<DailyEarning>>

    data class DailyEarning(val day: String, val total: Double)
}
