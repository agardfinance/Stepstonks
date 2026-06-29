package com.stepstonks.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepstonks.app.domain.model.TokenTransaction
import com.stepstonks.app.domain.model.TransactionType

@Entity(tableName = "token_transactions")
data class TokenTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val balanceAfter: Double = 0.0,
    val referenceId: String? = null
)

fun TokenTransactionEntity.toDomain() = TokenTransaction(
    id = id, type = TransactionType.valueOf(type), amount = amount,
    description = description, timestamp = timestamp,
    balanceAfter = balanceAfter, referenceId = referenceId
)

fun TokenTransaction.toEntity() = TokenTransactionEntity(
    id = id, type = type.name, amount = amount,
    description = description, timestamp = timestamp,
    balanceAfter = balanceAfter, referenceId = referenceId
)
