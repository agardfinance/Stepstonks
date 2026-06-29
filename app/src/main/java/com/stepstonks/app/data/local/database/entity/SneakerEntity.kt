package com.stepstonks.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepstonks.app.domain.model.Sneaker
import com.stepstonks.app.domain.model.SneakerAttribute
import com.stepstonks.app.domain.model.SneakerRarity
import com.stepstonks.app.domain.model.SneakerType

@Entity(tableName = "sneakers")
data class SneakerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val rarity: String,
    val type: String,
    val level: Int = 0,
    val maxLevel: Int = 30,
    val efficiency: Int = 0,
    val luck: Int = 0,
    val comfort: Int = 0,
    val resilience: Int = 0,
    val durability: Float = 100f,
    val isEquipped: Boolean = false,
    val mintCount: Int = 0,
    val maxMints: Int = 7,
    val ownerId: String = "",
    val acquiredAt: Long = System.currentTimeMillis()
)

fun SneakerEntity.toDomain() = Sneaker(
    id = id, name = name,
    rarity = SneakerRarity.valueOf(rarity), type = SneakerType.valueOf(type),
    level = level, maxLevel = maxLevel,
    attributes = SneakerAttribute(efficiency, luck, comfort, resilience),
    durability = durability, isEquipped = isEquipped,
    mintCount = mintCount, maxMints = maxMints,
    ownerId = ownerId, acquiredAt = acquiredAt
)

fun Sneaker.toEntity() = SneakerEntity(
    id = id, name = name, rarity = rarity.name, type = type.name,
    level = level, maxLevel = maxLevel,
    efficiency = attributes.efficiency, luck = attributes.luck,
    comfort = attributes.comfort, resilience = attributes.resilience,
    durability = durability, isEquipped = isEquipped,
    mintCount = mintCount, maxMints = maxMints,
    ownerId = ownerId, acquiredAt = acquiredAt
)
