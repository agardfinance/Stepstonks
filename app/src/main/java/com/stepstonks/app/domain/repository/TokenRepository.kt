package com.stepstonks.app.domain.repository

import com.stepstonks.app.domain.model.TokenTransaction
import com.stepstonks.app.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getTransactionHistory(): Flow<List<TokenTransaction>>
    fun getRecentTransactions(limit: Int): Flow<List<TokenTransaction>>
    suspend fun recordTransaction(transaction: TokenTransaction)
    suspend fun getTotalEarned(): Double
    suspend fun getTotalSpent(): Double
    fun getDailyEarnings(days: Int): Flow<List<Pair<String, Double>>>
}
