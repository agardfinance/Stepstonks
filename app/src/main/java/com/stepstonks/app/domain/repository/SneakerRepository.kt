package com.stepstonks.app.domain.repository

import com.stepstonks.app.domain.model.Sneaker
import kotlinx.coroutines.flow.Flow

interface SneakerRepository {
    fun getUserSneakers(): Flow<List<Sneaker>>
    fun getEquippedSneaker(): Flow<Sneaker?>
    suspend fun addSneaker(sneaker: Sneaker)
    suspend fun updateSneaker(sneaker: Sneaker)
    suspend fun equipSneaker(sneakerId: String)
    suspend fun unequipSneaker()
    suspend fun upgradeSneaker(sneakerId: String): Boolean
    suspend fun repairSneaker(sneakerId: String): Boolean
    suspend fun getSneakerById(id: String): Sneaker?
}
