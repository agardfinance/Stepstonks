package com.stepstonks.app.domain.repository

import com.stepstonks.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun addXp(userId: String, xp: Long)
    suspend fun addTokens(userId: String, amount: Double)
    suspend fun spendTokens(userId: String, amount: Double): Boolean
    suspend fun updateEnergy(userId: String, energy: Float)
    suspend fun updateStreak(userId: String, streak: Int)
    suspend fun equipSneaker(userId: String, sneakerId: String?)
    suspend fun getUserById(id: String): User?
    suspend fun isUsernameAvailable(username: String): Boolean
}
