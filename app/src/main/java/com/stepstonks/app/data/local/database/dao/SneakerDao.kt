package com.stepstonks.app.data.local.database.dao

import androidx.room.*
import com.stepstonks.app.data.local.database.entity.SneakerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SneakerDao {
    @Query("SELECT * FROM sneakers ORDER BY rarity DESC, level DESC")
    fun getAllSneakers(): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE isEquipped = 1 LIMIT 1")
    fun getEquippedSneaker(): Flow<SneakerEntity?>

    @Query("SELECT * FROM sneakers WHERE id = :id")
    suspend fun getSneakerById(id: String): SneakerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSneaker(sneaker: SneakerEntity)

    @Update
    suspend fun updateSneaker(sneaker: SneakerEntity)

    @Query("UPDATE sneakers SET isEquipped = 0")
    suspend fun unequipAll()

    @Query("UPDATE sneakers SET isEquipped = 1 WHERE id = :id")
    suspend fun equipSneaker(id: String)

    @Query("UPDATE sneakers SET level = level + 1 WHERE id = :id AND level < maxLevel")
    suspend fun upgradeLevel(id: String): Int

    @Query("UPDATE sneakers SET durability = MIN(100, durability + 100) WHERE id = :id")
    suspend fun repairDurability(id: String)

    @Query("UPDATE sneakers SET durability = MAX(0, durability - :amount) WHERE isEquipped = 1")
    suspend fun degradeEquippedDurability(amount: Float)
}
