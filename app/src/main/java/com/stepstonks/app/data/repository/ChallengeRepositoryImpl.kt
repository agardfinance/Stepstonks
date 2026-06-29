package com.stepstonks.app.data.repository

import com.stepstonks.app.data.local.database.dao.ChallengeDao
import com.stepstonks.app.data.local.database.entity.toDomain
import com.stepstonks.app.data.local.database.entity.toEntity
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.domain.repository.ChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDao: ChallengeDao
) : ChallengeRepository {

    override fun getActiveChallenges(): Flow<List<Challenge>> =
        challengeDao.getActiveChallenges().map { it.map { e -> e.toDomain() } }

    override fun getChallengesByType(type: ChallengeType): Flow<List<Challenge>> =
        challengeDao.getChallengesByType(type.name).map { it.map { e -> e.toDomain() } }

    override fun getAchievements(): Flow<List<Challenge>> =
        challengeDao.getAchievements().map { it.map { e -> e.toDomain() } }

    override suspend fun updateChallengeProgress(challengeId: String, value: Long) {
        challengeDao.updateProgress(challengeId, value)
        val challenge = challengeDao.getChallengeById(challengeId)
        if (challenge != null && value >= challenge.targetValue && challenge.status == ChallengeStatus.ACTIVE.name) {
            challengeDao.markCompleted(challengeId, System.currentTimeMillis())
        }
    }

    override suspend fun claimChallenge(challengeId: String): Challenge? {
        val entity = challengeDao.getChallengeById(challengeId) ?: return null
        if (entity.status != ChallengeStatus.COMPLETED.name) return null
        challengeDao.markClaimed(challengeId)
        return entity.copy(status = ChallengeStatus.CLAIMED.name).toDomain()
    }

    override suspend fun initializeChallengesForUser(userId: String) {
        val allChallenges = PREDEFINED_DAILY_CHALLENGES +
                PREDEFINED_WEEKLY_CHALLENGES +
                PREDEFINED_ACHIEVEMENTS
        challengeDao.insertChallenges(allChallenges.map { it.toEntity() })
    }

    override suspend fun resetDailyChallenges() = challengeDao.resetDailyChallenges()

    override suspend fun resetWeeklyChallenges() = challengeDao.resetWeeklyChallenges()

    override suspend fun getChallengeById(id: String): Challenge? =
        challengeDao.getChallengeById(id)?.toDomain()

    override fun getClaimedChallengesCount(): Flow<Int> = challengeDao.getClaimedCount()
}
