package com.stepstonks.app.data.repository

import com.stepstonks.app.data.local.database.dao.TokenDao
import com.stepstonks.app.data.local.database.entity.toDomain
import com.stepstonks.app.data.local.database.entity.toEntity
import com.stepstonks.app.domain.model.TokenTransaction
import com.stepstonks.app.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenDao: TokenDao
) : TokenRepository {

    override fun getTransactionHistory(): Flow<List<TokenTransaction>> =
        tokenDao.getAllTransactions().map { it.map { e -> e.toDomain() } }

    override fun getRecentTransactions(limit: Int): Flow<List<TokenTransaction>> =
        tokenDao.getRecentTransactions(limit).map { it.map { e -> e.toDomain() } }

    override suspend fun recordTransaction(transaction: TokenTransaction) =
        tokenDao.insertTransaction(transaction.toEntity())

    override suspend fun getTotalEarned(): Double = tokenDao.getTotalEarned()

    override suspend fun getTotalSpent(): Double = tokenDao.getTotalSpent()

    override fun getDailyEarnings(days: Int): Flow<List<Pair<String, Double>>> =
        tokenDao.getDailyEarnings(days).map { list -> list.map { it.day to it.total } }
}
