package com.stepstonks.app.data.repository

import com.stepstonks.app.data.local.database.dao.UserDao
import com.stepstonks.app.data.local.database.entity.toDomain
import com.stepstonks.app.data.local.database.entity.toEntity
import com.stepstonks.app.domain.model.User
import com.stepstonks.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getCurrentUser(): Flow<User?> =
        userDao.getCurrentUser().map { it?.toDomain() }

    override suspend fun createUser(user: User) =
        userDao.insertUser(user.toEntity())

    override suspend fun updateUser(user: User) =
        userDao.updateUser(user.toEntity())

    override suspend fun addXp(userId: String, xp: Long) =
        userDao.addXpAndSteps(userId, xp, 0)

    override suspend fun addTokens(userId: String, amount: Double) =
        userDao.addTokens(userId, amount)

    override suspend fun spendTokens(userId: String, amount: Double): Boolean =
        userDao.spendTokens(userId, amount) > 0

    override suspend fun updateEnergy(userId: String, energy: Float) =
        userDao.updateEnergy(userId, energy, System.currentTimeMillis())

    override suspend fun updateStreak(userId: String, streak: Int) =
        userDao.updateStreak(userId, streak)

    override suspend fun equipSneaker(userId: String, sneakerId: String?) =
        userDao.equipSneaker(userId, sneakerId, System.currentTimeMillis())

    override suspend fun getUserById(id: String): User? =
        userDao.getUserById(id)?.toDomain()

    override suspend fun isUsernameAvailable(username: String): Boolean =
        userDao.countByUsername(username) == 0
}
