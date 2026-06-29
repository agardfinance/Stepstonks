package com.stepstonks.app.domain.repository

import com.stepstonks.app.domain.model.Challenge
import com.stepstonks.app.domain.model.ChallengeStatus
import com.stepstonks.app.domain.model.ChallengeType
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    fun getActiveChallenges(): Flow<List<Challenge>>
    fun getChallengesByType(type: ChallengeType): Flow<List<Challenge>>
    fun getAchievements(): Flow<List<Challenge>>
    suspend fun updateChallengeProgress(challengeId: String, value: Long)
    suspend fun claimChallenge(challengeId: String): Challenge?
    suspend fun initializeChallengesForUser(userId: String)
    suspend fun resetDailyChallenges()
    suspend fun resetWeeklyChallenges()
    suspend fun getChallengeById(id: String): Challenge?
    fun getClaimedChallengesCount(): Flow<Int>
}
