package com.stepstonks.app.data.repository

import com.stepstonks.app.data.local.database.dao.SneakerDao
import com.stepstonks.app.data.local.database.entity.toDomain
import com.stepstonks.app.data.local.database.entity.toEntity
import com.stepstonks.app.domain.model.Sneaker
import com.stepstonks.app.domain.repository.SneakerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SneakerRepositoryImpl @Inject constructor(
    private val sneakerDao: SneakerDao
) : SneakerRepository {

    override fun getUserSneakers(): Flow<List<Sneaker>> =
        sneakerDao.getAllSneakers().map { it.map { e -> e.toDomain() } }

    override fun getEquippedSneaker(): Flow<Sneaker?> =
        sneakerDao.getEquippedSneaker().map { it?.toDomain() }

    override suspend fun addSneaker(sneaker: Sneaker) =
        sneakerDao.insertSneaker(sneaker.toEntity())

    override suspend fun updateSneaker(sneaker: Sneaker) =
        sneakerDao.updateSneaker(sneaker.toEntity())

    override suspend fun equipSneaker(sneakerId: String) {
        sneakerDao.unequipAll()
        sneakerDao.equipSneaker(sneakerId)
    }

    override suspend fun unequipSneaker() = sneakerDao.unequipAll()

    override suspend fun upgradeSneaker(sneakerId: String): Boolean =
        sneakerDao.upgradeLevel(sneakerId) > 0

    override suspend fun repairSneaker(sneakerId: String): Boolean {
        sneakerDao.repairDurability(sneakerId)
        return true
    }

    override suspend fun getSneakerById(id: String): Sneaker? =
        sneakerDao.getSneakerById(id)?.toDomain()
}
