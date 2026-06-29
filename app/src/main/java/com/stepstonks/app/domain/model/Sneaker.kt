package com.stepstonks.app.domain.model

enum class SneakerRarity(
    val displayName: String,
    val earningMultiplier: Double,
    val maxEnergyBonus: Float,
    val colorHex: String,
    val mintCost: Double
) {
    COMMON("Common", 1.0, 0f, "#808080", 0.0),
    UNCOMMON("Uncommon", 1.5, 2f, "#00C853", 100.0),
    RARE("Rare", 2.0, 4f, "#2979FF", 300.0),
    EPIC("Epic", 2.5, 6f, "#AA00FF", 800.0),
    LEGENDARY("Legendary", 3.0, 10f, "#FFD700", 2000.0)
}

enum class SneakerType(val displayName: String, val speedRange: String) {
    WALKER("Walker", "1-6 km/h"),
    JOGGER("Jogger", "4-10 km/h"),
    RUNNER("Runner", "8-20 km/h"),
    TRAINER("Trainer", "1-20 km/h")
}

data class SneakerAttribute(
    val efficiency: Int = 0,
    val luck: Int = 0,
    val comfort: Int = 0,
    val resilience: Int = 0
) {
    val total: Int get() = efficiency + luck + comfort + resilience
}

data class Sneaker(
    val id: String,
    val name: String,
    val rarity: SneakerRarity,
    val type: SneakerType,
    val level: Int = 0,
    val maxLevel: Int = 30,
    val attributes: SneakerAttribute = SneakerAttribute(),
    val durability: Float = 100f,
    val isEquipped: Boolean = false,
    val mintCount: Int = 0,
    val maxMints: Int = 7,
    val ownerId: String = "",
    val acquiredAt: Long = System.currentTimeMillis(),
    val imageRes: String = ""
) {
    val earningRate: Double get() = rarity.earningMultiplier * (1 + attributes.efficiency * 0.01)
    val luckBonus: Double get() = attributes.luck * 0.005
    val energyBonus: Float get() = rarity.maxEnergyBonus + attributes.comfort * 0.1f
    val durabilityPercent: Float get() = (durability / 100f).coerceIn(0f, 1f)
    val canMint: Boolean get() = mintCount < maxMints
    val levelProgress: Float get() = (level.toFloat() / maxLevel.toFloat()).coerceIn(0f, 1f)
}

val STARTER_SNEAKER = Sneaker(
    id = "starter_common_walker",
    name = "Basic Kicks",
    rarity = SneakerRarity.COMMON,
    type = SneakerType.WALKER,
    attributes = SneakerAttribute(efficiency = 2, luck = 1, comfort = 1, resilience = 2)
)

val SNEAKER_SHOP_ITEMS = listOf(
    Sneaker("shop_uncommon_walker", "Green Gliders", SneakerRarity.UNCOMMON, SneakerType.WALKER, attributes = SneakerAttribute(5, 3, 4, 3)),
    Sneaker("shop_rare_jogger", "Blue Bolts", SneakerRarity.RARE, SneakerType.JOGGER, attributes = SneakerAttribute(8, 5, 6, 6)),
    Sneaker("shop_epic_runner", "Purple Phantoms", SneakerRarity.EPIC, SneakerType.RUNNER, attributes = SneakerAttribute(12, 8, 9, 8)),
    Sneaker("shop_legendary_trainer", "Golden Gods", SneakerRarity.LEGENDARY, SneakerType.TRAINER, attributes = SneakerAttribute(18, 14, 14, 14)),
)
